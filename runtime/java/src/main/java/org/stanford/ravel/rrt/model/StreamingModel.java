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
public abstract class StreamingModel<RecordType extends ModelRecord> extends BaseModel<RecordType> {
    private final List<Integer> mSinkEndpoints = new ArrayList<>();
    private final List<Integer> mSourceEndpoints = new ArrayList<>();

    protected StreamingModel(DispatcherAPI dispatcher, int modelId, int size, boolean reliable, boolean durable) {
        super(dispatcher, modelId, size, reliable, durable);
    }

    public void addSinkEndpoints(Collection<Integer> e) {
        mSinkEndpoints.addAll(e);
    }
    public void addSourceEndpoints(Collection<Integer> e) {
        mSourceEndpoints.addAll(e);
    }

    void pprint(String s) {
        System.out.println("[StreamingModel::]>" + s);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        // save locally first
        Context<RecordType> local = doSave(record, true);
        if (local.error == Error.SUCCESS) {
            int recordPos = recordPosFromRecord(record);

            Error sendError = sendRecord(recordPos, record, mSinkEndpoints, false);

            // clear the save flag because we won't send a save done until later
            markSaved(recordPos);

            return new Context<>(this, record, sendError);
        } else {
            // OUT OF STORAGE or IN TRANSIT (= during save)
            return local;
        }
    }

    @Override
    public void delete(RecordType record) {
        if (doDelete(record) && isDurable()) {
            // TODO remove from durable storage
        }
    }

    @Override
    public void recordSavedDurably(RavelPacket pkt, Error error) {
        RecordType record = savedDurably(pkt);
        if (record == null)
            return;

        int recordPos = recordPosFromRecord(record);
        if (isArrived(recordPos)) {
            Endpoint arrivedFrom = getArrivedFrom(recordPos);
            markArrived(recordPos, false, null);
            notifyArrived(new Context<>(this, record, Error.SUCCESS, arrivedFrom));

            forward(pkt, record);
        } else {
            sendRecord(recordPos, record, mSinkEndpoints, false);
            // ignore errors
            // if we're reliable, we'll retry with a timeout
        }
    }

    @Override
    public void recordArrived(RavelPacket pkt, Endpoint endpoint) {
        if (pkt.isDelete()) {
            // do nothing
        } else if (pkt.isAck()) {
            int recordPos = findRecordWithId(pkt.record_id);
            if (recordPos < 0 || isValid(recordPos))
                return;

            if (isReliable() && handleAck(recordPos))
                notifyDeparted(new Context<>(this, recordAt(recordPos)));
        } else if (pkt.isSaveDone()) {
            // forward the save done no matter what
            forward(pkt, null);

            int recordPos = findRecordWithId(pkt.record_id);
            if (recordPos < 0 || !isValid(recordPos))
                return;

            notifySaveDone(new Context<>(this, recordAt(recordPos)));
        } else {
            // send the ack first
            if (isReliable()) {
                RavelPacket ack = RavelPacket.makeAck(pkt.model_id, pkt.record_id);
                dispatcher.model__sendData(ack, endpoint);
            }

            int recordPos = findRecordWithId(pkt.record_id);
            if (recordPos >= 0) {
                // duplicate record (spurious retransmission)
                // ignore
                return;
            }

            RecordType record = create();
            Context<RecordType> ctx = handleRecord(record, pkt, endpoint);
            ctx.endpoint = endpoint;

            if (ctx.error == Error.SUCCESS) {
                recordPos = recordPosFromRecord(record);
                markSaved(recordPos);

                notifyArrived(ctx);
                forward(pkt, record);
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

    private Error forward(RavelPacket pkt, RecordType record) {
        Collection<Integer> endpointNames;

        if (pkt.isSaveDone()) {
            endpointNames = mSourceEndpoints;
        } else {
            endpointNames = mSinkEndpoints;
        }

        if (endpointNames.isEmpty()) {
            if (!pkt.isSaveDone()) {
                RavelPacket saveDone = RavelPacket.makeSaveDone(pkt.model_id, pkt.record_id);
                return forwardPacket(saveDone, mSourceEndpoints, null);
            } else {
                return Error.SUCCESS;
            }
        }

        return forwardPacket(pkt, endpointNames, record);
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

        // retransmit any other record that was busy
        retransmit(endpoint);
    }

    private void retransmit(Endpoint ep) {
        for (int i = 0; i < getModelSize(); i++) {
            if (isValid(i) && isTransmitFailed(i) && !isInTransit(i)) {
                RecordType record = recordAt(i);
                sendOneRecord(i, record, ep, false);
            }
        }
    }

    @Override
    public void recordFailedToSend(RavelPacket pkt, Endpoint endpoint, Error error) {
        if (pkt.isAck() || pkt.isSaveDone()) {
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

        // balance the retransmission increasing the counter
        if (isReliable())
            handleAck(recordPos);

        // retransmit any other record that was busy
        retransmit(endpoint);
    }
}
