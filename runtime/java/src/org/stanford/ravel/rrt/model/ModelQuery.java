package org.stanford.ravel.rrt.model;

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
     * The unused argument is to be able to dynamically create an array of the right
     * record type.
     * In practice, you should the version of all() with no arguments provided
     * by the concrete model subclass, which is not polymorphic.
     *
     * @return pointers to all records
     */
    RecordType[] all(RecordType[] unused);

    /**
     * Clears local model storage
     */
    void clear();
}
