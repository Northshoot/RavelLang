package org.stanford.ravel.rrt.model;

import patterns.src.java.model.Model;

/**
 * The interface to query the model, exposed to Ravel on the model object
 * (not yet actually, but maybe in the future)
 *
 * Created by lauril on 1/23/17.
 */
public interface ModelQuery<RecordType> {

    /**
     * Queries local model storage
     *
     * @return pointer to the first record
     */
    RecordType first();

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
     * @return getFirst()
     */
    RecordType get(int x);

    /**
     * Queries local model storage
     *
     * @return pointers to all records
     */
    // FIXME this needs to be Object because ArrayList.toArray cannot
    // be generic otherwise
    Object[] all();

    /**
     * Clears local model storage
     */
    void clear();
}
