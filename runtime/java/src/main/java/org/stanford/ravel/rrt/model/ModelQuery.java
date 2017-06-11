package org.stanford.ravel.rrt.model;

import org.jetbrains.annotations.Nullable;
import org.stanford.ravel.rrt.tiers.Endpoint;

import java.lang.annotation.Native;
import java.util.Iterator;

/**
 * The interface to query the model, exposed to Ravel on the model object
 * (not yet actually, but maybe in the future)
 *
 * Created by lauril on 1/23/17.
 */
public interface ModelQuery<RecordType> extends Iterable<RecordType> {

    /**
     * Queries local model storage
     *
     * @return pointer to the first record
     */
    RecordType first(Endpoint endpoint);

    /**
     * Queries local model storage
     *
     * @return pointer to the last record
     */
    RecordType last();

    /**
     * Queries local model storage
     *  if x >= 0 && x <= current_pos
     * @return pointer to the x's record
     * else
     * @return record
     */
    RecordType get(int x);

    /**
     * Queries local model storage
     *
     * The unused argument is to be able to dynamically create an array of the right
     * record type.
     * In practice, you should the version of all() with no arguments provided
     * by the concrete model subclass, which is not polymorphic.
     *
     * @return pointers to all records
     */
    RecordType[] all(RecordType[] unused);
    Iterator<RecordType> iterator();

    /**
     * Clears local model storage or endpoint storage
     */
    void clear(@Nullable Endpoint endpoint);

    /**
     * clears all model storage
     */
    void clearAll();


    /**
     * Returns the number of records in this model or endpoint model
     *
     * @return the model size
     */
    int size(@Nullable Endpoint endpoint);

}
