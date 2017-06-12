package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

import java.util.*;

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
        pprint_base("StreamingModel" , s);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        // save locally first
        int src = dispatcher.getDeviceId();
        record.device_id(src);
        Context<RecordType> local = doSave(record, src, true);
        //this just addressing rise condition, when after adding a record clearAll is called
        //TODO: should formalize soliution for these events
        int recordPos = recordPosFromRecord(record, src);
        //if success send record
        if (local.error == Error.SUCCESS && recordPos >= 0) {
            // clear the save flag because we won't send a save done until later
            markSaved(src, recordPos);
            Error sendError = sendRecord(recordPos, src, record, mSinkEndpoints, false);
            return new Context<>(this, record, sendError);
        } else {
            // OUT OF STORAGE or IN TRANSIT (= during save)
            return local;
        }
    }

    @Override
    public void delete(RecordType record, int src) {
        //TODO: need redefine how delete works
        if (doDelete(record, src) && isDurable()) {
            // TODO remove from durable storage
        }
    }

    @Override
    public void recordSavedDurably(RavelPacket pkt, Error error) {
        RecordType record = savedDurably(pkt);
        int src = pkt.getSource();
        if (record == null)
            return;

        int recordPos = recordPosFromRecord(record,src);
        if (isArrived(src, recordPos)) {
            Endpoint arrivedFrom = getArrivedFrom(src, recordPos);
            markArrived(src, recordPos, false, null);
            notifyArrived(new Context<>(this, record, Error.SUCCESS, arrivedFrom));

            forward(pkt, record);
        } else {
            sendRecord(recordPos, src, record, mSinkEndpoints, false);
            // ignore errors
            // if we're reliable, we'll retry with a timeout
        }
    }

    @Override
    public synchronized void  recordArrived(RavelPacket pkt, Endpoint endpoint) {
        int src = pkt.getSource();
        System.out.println("recordArrived: " + pkt.toString());
        if (pkt.isDelete()) {
            // do nothing
        } else if (pkt.isAck()) {
            int recordPos = findRecordWithId(pkt.getSource(), pkt.record_id);
            if (recordPos < 0 || isValid(src, recordPos))
                return;

            if (isReliable() && handleAck(src, recordPos))
                notifyDeparted(new Context<>(this, recordAt(src, recordPos)));
        } else if (pkt.isSaveDone()) {
            // forward the save done no matter what
            forward(pkt, null);

            int recordPos = findRecordWithId(src, pkt.record_id);
            if (recordPos < 0 || !isValid(src, recordPos))
                return;

            notifySaveDone(new Context<>(this, recordAt(src, recordPos)));
        } else {
            // send the ack first
            if (isReliable()) {
                RavelPacket ack = RavelPacket.makeAck(pkt.model_id, pkt.record_id);
                dispatcher.model__sendData(ack, endpoint);
            }

            int recordPos = findRecordWithId(src, pkt.record_id);
            if (recordPos >= 0) {
                // duplicate record (spurious retransmission)
                // ignore
                return;
            }
            RecordType record = create();
            Context<RecordType> ctx = handleRecord(record, pkt, endpoint);
            ctx.endpoint = endpoint;

            if (ctx.error == Error.SUCCESS) {
                recordPos = recordPosFromRecord(record, src);
                markSaved(src, recordPos);
                System.out.println("mValidRecordsFlowMap size "+ mValidRecordsFlowMap.size());
                System.out.println("mRecordFlowMap size "+ mRecordFlowMap.size());
                forward(pkt, record);
                notifyArrived(ctx);

            } else if (ctx.error == Error.IN_TRANSIT) {
                // saving, wait until done saving to tell the app
                recordPos = recordPosFromRecord(record, src);

                assert isValid(src, recordPos);
                markArrived(src, recordPos, true, endpoint);
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
//                System.out.println("make send done : " + pkt.toString());
                RavelPacket saveDone = RavelPacket.makeSaveDone(dispatcher.getAppId(),
                                                                pkt.getTier(),
                                                                dispatcher.getDeviceId(),
                                                                pkt.model_id, pkt.record_id);
//                System.out.println("send done pkt: " + saveDone.toString());
                return forwardPacket(saveDone, pkt.getSource(), null);
            } else {
                return Error.SUCCESS;
            }
        }
    //todo fix me
        return forwardPacket(pkt, pkt.getSource(), record);
    }

    @Override
    public void recordDeparted(RavelPacket pkt, Endpoint endpoint) {
        int src = pkt.getSource();
        if (pkt.isAck() || pkt.isSaveDone()) {
            return;
        }
        //FIXME need to be addressed
        int recordPos  = findRecordWithId(src, pkt.record_id);
        if (recordPos <0 ){
            //TODO: need an formal flow description
            //record has been deleted while departing,
            return;
        }
        // we cannot free stuff that is in transit
        RecordType record;
        record = recordAt(src, recordPos);

        if (markNotInTransit(src, recordPos)) {
            if (!isValid(src, recordPos)) {
                // record was deleted after sending
                freeRecord(src, recordPos);
            } else {
                markTransmitNotFailed(src, recordPos);

                if (isReliable()) {
                    // do nothing and wait for the acks
                } else {

                    notifyDeparted(new Context<>(this, record, Error.SUCCESS, endpoint));
                }
            }
        }

        // retransmit any other record that was busy
        retransmit(endpoint);
    }

    private void retransmit(Endpoint ep) {
        for(Map.Entry<Integer, ArrayList<RecordType>> entry : mRecordFlowMap.entrySet()) {
            Integer src = entry.getKey();
            for (int i = 0; i < getModelSize(); i++) {
                if (isValid(src, i) && isTransmitFailed(src, i) && !isInTransit(src, i)) {
                    RecordType record = recordAt(src, i);
                    sendOneRecord(i, src, record, ep, false);
                }
            }
        }
    }

    @Override
    public void recordFailedToSend(RavelPacket pkt, Endpoint endpoint, Error error) {
        int src = pkt.getSource();
        if (pkt.isAck() || pkt.isSaveDone()) {
            // unconditionally try to retransmit
            if (endpoint.isConnected())
                dispatcher.model__sendData(pkt, endpoint);
            return;
        }

        int recordPos = findRecordWithId(src, pkt.record_id);
        if(recordPos <0 ){
            //record has been deleted
            return;
        }
        //RecordType record = recordAt(src, recordPos);

        if (markNotInTransit(src, recordPos) && !isValid(src, recordPos)) {
            // if the record was deleted after sending, no matter what we're
            // not going to try resending
            freeRecord(src, recordPos);
            return;
        }

        // balance the retransmission increasing the counter
        if (isReliable())
            handleAck(src, recordPos);

        // retransmit any other record that was busy
        retransmit(endpoint);
    }
}
