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
    private final List<Endpoint> mEndpoints = new ArrayList<>();

    protected StreamingModel(DispatcherAPI dispatcher, int size) {
        super(dispatcher, size);
    }

    public void addEndpoints(Collection<Endpoint> e) {
        mEndpoints.addAll(e);
    }

    void pprint(String s){
        System.out.println("[StreamingModel::]>" + s);
    }

    private Error sendOne(RavelPacket pkt, Endpoint e) {
        return mDispatcher.model__sendData(pkt, e);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        // save locally first
        Context<RecordType> local = addRecord(record);
        if (local.error != Error.SUCCESS)
            return local;

        // Packetize the record and send it
        byte[] rec = record.toBytes();
        RavelPacket pkt = RavelPacket.fromRecord(rec);

        Error error = Error.SUCCESS;
        for (Endpoint e : mEndpoints) {
            Error error2 = sendOne(pkt, e);
            if (error2 != Error.SUCCESS)
                error = error2;
        }
        if (error != Error.SUCCESS)
            return new Context<>(this, error);
        else
            return new Context<>(this, record);
    }
}
