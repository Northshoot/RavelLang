package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by gcampagn on 1/30/17.
 */
public abstract class StreamingModel<RecordType extends ModelRecord> extends BaseModel<RecordType> {
    private Endpoint mEndpoint;

    protected StreamingModel(DispatcherAPI dispatcher, int size) {
        super(dispatcher, size);
    }

    public void setEndpoint(Endpoint ep) {
        mEndpoint = ep;
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        if (! mEndpoint.isConnected() ){
            //TODO: queue packets
            Context<RecordType> ctx = addRecord(record);
            if (ctx.hasError())
                return ctx;
        }

        // Packetize the record and send it
        // determine and send to endpoints
        Error error = mDispatcher.send_data(record.toBytes(), mEndpoint);
        if (error != Error.SUCCESS)
            return new Context<>(this, error);
        else
            return new Context<>(this, record);
    }
}
