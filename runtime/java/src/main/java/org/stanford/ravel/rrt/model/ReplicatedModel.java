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

    protected ReplicatedModel(DispatcherAPI dispatcher, int size, boolean reliable, boolean durable) {
        super(dispatcher, size, reliable, durable);
    }

    void pprint(String s){
        System.out.println("[ReplicatedModel::]>" + s);
    }

    public void addSinkEndpoints(Collection<String> e) {
        mEndpoints.addAll(e);
    }
    public void addSourceEndpoints(Collection<String> e) {}

    @Override
    public Context<RecordType> save(RecordType record) {
        // TODO
        return new Context<RecordType>(this, Error.SYSTEM_ERROR);
    }

    @Override
    public void delete(RecordType record) {
        // TODO
    }

    @Override
    public void recordSavedDurably(RavelPacket pkt, Error error) {
        // TODO
    }

    @Override
    public void recordArrived(RavelPacket pkt, Endpoint endpoint) {
        // TODO
    }

    @Override
    public void recordDeparted(RavelPacket pkt, Endpoint endpoint) {
        // TODO
    }

    @Override
    public void recordFailedToSend(RavelPacket pkt, Endpoint endpoint, Error error) {
        // TODO
    }
}
