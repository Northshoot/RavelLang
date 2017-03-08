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
    private final List<String> mEndpoints = new ArrayList<>();

    protected ReplicatedModel(DispatcherAPI dispatcher, int size) {
        super(dispatcher, size);
    }

    void pprint(String s){
        System.out.println("[ReplicatedModel::]>" + s);
    }

    public void addEndpoints(Collection<String> e) {
        mEndpoints.addAll(e);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        // save locally first
        Context<RecordType> local = addRecord(record);
        if (local.error != Error.SUCCESS)
            return local;

        // Packetize the record and send it
        return sendRecord(record, mEndpoints);
    }

    @Override
    public void recordFailedToSend(RavelPacket pkt, Endpoint endpoint, Error error) {
        // FIXME what to do in this case?
        // for now, drop on the floor
        markRecordArrived(pkt.record_id);
    }

    @Override
    public void recordAcknowledged(int recordId) {
        // FIXME what to do in this case?
        // for now, ignore the ack except for bookkeeping
    }
}
