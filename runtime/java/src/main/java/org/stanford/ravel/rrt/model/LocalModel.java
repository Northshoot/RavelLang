package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by gcampagn on 1/30/17.
 */
public abstract class LocalModel<RecordType extends ModelRecord> extends BaseModel<RecordType> {
    protected LocalModel(DispatcherAPI dispatcher, int modelId, int size, boolean reliable, boolean durable) {
        super(dispatcher, modelId, size, reliable, durable);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        Context<RecordType> ctx = doSave(record, true);
        if (ctx.error == Error.SUCCESS)
            queueSaveDone(record);
        return ctx;
    }

    @Override
    public void recordArrived(RavelPacket pkt, Endpoint endpoint) {
        throw new AssertionError("Local models should not receive network events");
    }

    @Override
    public void recordDeparted(RavelPacket pkt, Endpoint endpoint) {
        throw new AssertionError("Local models should not receive network events");
    }

    @Override
    public void recordFailedToSend(RavelPacket pkt, Endpoint endpoint, Error error) {
        throw new AssertionError("Local models should not receive network events");
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

        notifySaveDone(new Context<>(this, record));
    }
}
