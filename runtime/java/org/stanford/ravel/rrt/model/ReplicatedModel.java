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
    private int index = 0;

    private List<Endpoint> mEndpoints = new ArrayList<>();

    protected ReplicatedModel(DispatcherAPI dispatcher, int size) {
        super(dispatcher, size);
    }

    protected void addEndpoint(Endpoint e) {
        mEndpoints.add(e);
    }

    private Error sendOne(RavelPacket pkt, Endpoint e) {
        return mDispatcher.model__sendData(pkt, e);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        record.index(++index);

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
