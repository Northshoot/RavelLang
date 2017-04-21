package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gcampagn on 1/30/17.
 */
public abstract class ReplicatedModel<RecordType extends ModelRecord> extends BaseModel<RecordType> {
    private final List<Integer> mEndpoints = new ArrayList<>();

    protected ReplicatedModel(DispatcherAPI dispatcher, int modelId, int size, boolean reliable, boolean durable) {
        super(dispatcher, modelId, size, reliable, durable);
    }

    void pprint(String s){
        System.out.println("[ReplicatedModel::]>" + s);
    }

    public void addSinkEndpoints(Collection<Integer> e) {
        mEndpoints.addAll(e);
    }
    public void addSourceEndpoints(Collection<Integer> e) {}

    @Override
    public Context<RecordType> save(RecordType record) {
        // save locally first
        Context<RecordType> local = doSave(record, true);
        if (local.error == Error.SUCCESS) {
            int recordPos = recordPosFromRecord(record);

            // clear the save flag because we won't send a save done until later
            markSaved(recordPos);

            Error sendError = sendRecord(recordPos, record, mEndpoints, true);
            return new Context<>(this, record, sendError);
        } else {
            // OUT OF STORAGE or IN TRANSIT (= during save)
            return local;
        }
    }

    public Context<RecordType> save(RecordType record, Endpoint endpoint) {
        // save locally first
        Context<RecordType> local = doSave(record, true);
        if (local.error == Error.SUCCESS) {
            int recordPos = recordPosFromRecord(record);

            // clear the save flag because we won't send a save done until later
            Error sendError = null;
            markSaved(recordPos);
            if (endpoint == null) {
                sendError = sendRecord(recordPos, record, mEndpoints, true);
            } else {
                    sendError = sendOneRecord(recordPos, record, endpoint, true);
            }
            return new Context<>(this, record, sendError);
        } else {
            // OUT OF STORAGE or IN TRANSIT (= during save)
            return local;
        }
    }

    @Override
    public void delete(RecordType record) {
        if (doDelete(record)) {
            if (isDurable()) {
                // TODO remove from durable storage
            }

            RavelPacket packet = RavelPacket.makeDelete(mModelId, record.index());
            forwardPacket(packet, mEndpoints, null);
        }
    }

    private void sendSaveDone(RavelPacket pkt, Endpoint origin) {
        RavelPacket saveDone = RavelPacket.makeSaveDone(pkt.model_id, pkt.record_id);
        dispatcher.model__sendData(saveDone, origin);
    }

    @Override
    public void recordSavedDurably(RavelPacket pkt, Error error) {
        RecordType record = savedDurably(pkt);
        if (record == null)
            return;

        int recordPos = recordPosFromRecord(record);
        if (isArrived(recordPos)) {
            Endpoint arrivedFrom = getArrivedFrom(recordPos);
            sendSaveDone(pkt, arrivedFrom);
            markArrived(recordPos, false, null);
            notifyArrived(new Context<>(this, record, Error.SUCCESS, arrivedFrom));

        } else {
            sendRecord(recordPos, record, mEndpoints, true);
            // ignore errors
            // if we're reliable, we'll retry with a timeout
        }
    }

    @Override
    public void recordArrived(RavelPacket pkt, Endpoint endpoint) {
        if (pkt.isDelete()) {
            int recordPos = findRecordWithId(pkt.record_id);
            if (recordPos < 0 || isValid(recordPos))
                return;

            doDelete(recordAt(recordPos));
        } else if (pkt.isAck()) {
            int recordPos = findRecordWithId(pkt.record_id);
            if (recordPos < 0 || isValid(recordPos))
                return;

            if (isReliable() && handleAck(recordPos))
                notifyDeparted(new Context<>(this, recordAt(recordPos)));
        } else if (pkt.isSaveDone()) {
            int recordPos = findRecordWithId(pkt.record_id);
            if (recordPos < 0 || !isValid(recordPos))
                return;

            if (markSaved(recordPos)) {
                notifySaveDone(new Context<>(this, recordAt(recordPos)));
            }
        } else {
            // send the ack first
            if (isReliable()) {
                RavelPacket ack = RavelPacket.makeAck(pkt.model_id, pkt.record_id);
                dispatcher.model__sendData(ack, endpoint);
            }

            int recordPos = findRecordWithId(pkt.record_id);
            RecordType record;
            if (recordPos >= 0) {
                record = recordAt(recordPos);
            } else {
                record = create();
            }
            Context<RecordType> ctx = handleRecord(record, pkt, endpoint);
            ctx.endpoint = endpoint;

            if (ctx.error == Error.SUCCESS) {
                recordPos = recordPosFromRecord(record);
                markSaved(recordPos);
                sendSaveDone(pkt, endpoint);

                notifyArrived(ctx);
            } else if (ctx.error == Error.IN_TRANSIT) {
                // saving, wait until done saving to tell the app
                recordPos = recordPosFromRecord(record);

                assert isValid(recordPos);
                markArrived(recordPos, true, endpoint);
            } else {
                // security error or some other error, nothing to do
            }
        }
    }

    @Override
    public void recordDeparted(RavelPacket pkt, Endpoint endpoint) {
        if (pkt.isAck() || pkt.isSaveDone()) {
            return;
        }

        int recordPos = findRecordWithId(pkt.record_id);
        // we cannot free stuff that is in transit
        assert recordPos >= 0;

        RecordType record = recordAt(recordPos);

        if (markNotInTransit(recordPos)) {
            if (!isValid(recordPos)) {
                // record was deleted after sending
                freeRecord(recordPos);
            } else {
                markTransmitNotFailed(recordPos);

                if (isReliable()) {
                    // do nothing and wait for the acks
                } else {
                    notifyDeparted(new Context<>(this, record));
                }
            }
        }
    }

    @Override
    public void recordFailedToSend(RavelPacket pkt, Endpoint endpoint, Error error) {
        if (pkt.isAck() || pkt.isSaveDone() || pkt.isDelete()) {
            // unconditionally try to retransmit
            if (endpoint.isConnected())
                dispatcher.model__sendData(pkt, endpoint);
            return;
        }

        int recordPos = findRecordWithId(pkt.record_id);
        // we cannot free stuff that is in transit
        assert recordPos >= 0;

        RecordType record = recordAt(recordPos);

        if (markNotInTransit(recordPos) && !isValid(recordPos)) {
            // if the record was deleted after sending, no matter what we're
            // not going to try resending
            freeRecord(recordPos);
            return;
        }

        // balance the retransmission increasing the counters
        if (isReliable())
            handleAck(recordPos);
        markSaved(recordPos);
        sendOneRecord(recordPos, record, endpoint, true);
    }
}
