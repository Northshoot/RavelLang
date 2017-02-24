package org.stanford.ravel.rrt.model;

/**
 * Created by gcampagn on 1/30/17.
 */
public interface ModelRecord {
    byte[] toBytes();

    void index(int i);

    int index();
}
