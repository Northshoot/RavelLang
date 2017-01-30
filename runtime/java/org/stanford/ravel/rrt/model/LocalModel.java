package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by gcampagn on 1/30/17.
 */
public abstract class LocalModel<RecordType> extends BaseModel<RecordType> {
    protected LocalModel(DispatcherAPI dispatcher, int size) {
        super(dispatcher, size);
    }

    @Override
    public Context<RecordType> save(RecordType record) {
        return addRecord(record);
    }
}
