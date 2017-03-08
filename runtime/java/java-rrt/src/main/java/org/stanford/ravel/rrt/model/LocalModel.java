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
    protected LocalModel(DispatcherAPI dispatcher, int size) {
        super(dispatcher, size);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        if (record.index() != -1) {
            // the record was already saved once
            return new Context<>(this, record);
        }

        Context<RecordType> ctx = addRecord(record);
        if (ctx.error != Error.SUCCESS)
            return ctx;

        markRecordAtRest(record.index());
        return ctx;
    }

    @Override
    public void recordFailedToSend(RavelPacket pkt, Endpoint endpoint, Error error) {
        throw new AssertionError("Local models should not receive network errors");
    }

    @Override
    void recordAcknowledged(int recordId) {
        throw new AssertionError("Local models should not receive network acks");
    }
}
