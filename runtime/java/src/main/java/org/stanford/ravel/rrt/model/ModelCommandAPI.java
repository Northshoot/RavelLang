package org.stanford.ravel.rrt.model;

import org.jetbrains.annotations.Nullable;
import org.stanford.ravel.rrt.Context;

/**
 * The runtime representation of a Model, as visible from the controller,
 * in the Java runtime.
 *
 * Created by gcampagn on 1/29/17.
 */
public interface ModelCommandAPI<RecordType extends ModelRecord> {
    /**
     * Saves the record and initiates transmission
     * @param record
     * @return
     */
    Context<RecordType> save(RecordType record);

    /**
     * Removes a record from storage
     * src defines flow number
     */
    void delete(RecordType record, @Nullable int src);


}
