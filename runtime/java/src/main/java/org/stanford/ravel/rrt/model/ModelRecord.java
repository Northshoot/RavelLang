package org.stanford.ravel.rrt.model;

/**
 * Created by Laurynas 23/5/2017 20:24:19
 */
public interface ModelRecord {
    /**
     * set index of the record
     * @param i
     */
    void index(int i);

    int index();
    /**
     * set device id
     * @param i
     */
    void device_id(int i);

    int device_id();

}
