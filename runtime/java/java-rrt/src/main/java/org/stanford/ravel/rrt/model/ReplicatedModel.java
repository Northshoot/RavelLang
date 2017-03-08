package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.DispatcherAPI;

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
}
