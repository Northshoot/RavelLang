package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by gcampagn on 1/30/17.
 */
public abstract class ReplicatedModel<RecordType extends ModelRecord> extends BaseModel<RecordType> {
    private Endpoint mEndpoint;

    protected ReplicatedModel(DispatcherAPI dispatcher, int size) {
        super(dispatcher, size);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        if (! mEndpoint.isConnected() ){
            //TODO: queue packets
            Context<RecordType> ctx = addRecord(record);
            if (ctx.hasError())
                return ctx;
        }

        //TODO: Packetize the record and send it
        //TODO: determine and send to endpoints
        return new Context<>(this, Error.WRITE_ERROR);
    }
}
