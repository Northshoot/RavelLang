package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
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

    void pprint(String s){
        System.out.println("[StreamingModel::]>" + s);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        pprint("save");
        if (! mEndpoint.isConnected() ){
            //TODO: queue packets
            Context<RecordType> ctx = addRecord(record);
            if (ctx.hasError())
                return ctx;
        }

        // Packetize the record and send it
        RavelPacket ravelPacket = new RavelPacket();
        ravelPacket.fromRecord(record.toBytes());

        pprint("pkt to send: " + ravelPacket);
        // determine and send to endpoints
        Error error = mDispatcher.send_data(ravelPacket, mEndpoint);
        if (error != Error.SUCCESS)
            return new Context<>(this, error);
        else
            return new Context<>(this, record);
    }
}
