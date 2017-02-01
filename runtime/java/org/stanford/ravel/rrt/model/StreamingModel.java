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
    protected Endpoint mEndpointDown = null;
    protected Endpoint mEndpointUpp = null;

    private int index = 0;
    protected StreamingModel(DispatcherAPI dispatcher, int size) {
        super(dispatcher, size);
    }

    public void setEndpointDown(Endpoint endpoint) {
        this.mEndpointDown = endpoint;
    }
    public void setEndpointUpp(Endpoint endpoint) {
        this.mEndpointUpp = endpoint;
    }

    void pprint(String s){
        System.out.println("[StreamingModel::]>" + s);
    }


    @Override
    public Context<RecordType> save(RecordType record) {
        pprint("save");
//        if (! mEndpoint.isConnected() ){
//            //TODO: queue packets
//            Context<RecordType> ctx = addRecord(record);
//            if (ctx.hasError())
//                return ctx;
//        }
        record.index(++index);
        // Packetize the record and send it
        byte[] rec = record.toBytes();
        RavelPacket ravelPacket = new RavelPacket(rec.length);
        ravelPacket.fromRecord(rec);


        // determine and send to endpoints
        Error error = null;
        //TODO: this is fast hack
        if(mEndpointDown != null) {
            error = mDispatcher.model__sendData(ravelPacket, mEndpointDown);
        }
        if(mEndpointUpp != null){
            error = mDispatcher.model__sendData(ravelPacket, mEndpointUpp);
        }
        if (error != Error.SUCCESS)
            return new Context<>(this, error);
        else
            return new Context<>(this, record);
    }
}
