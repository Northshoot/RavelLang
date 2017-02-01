package org.stanford.ravel.rrt;

/**
 * Created by lauril on 1/31/17.
 */
public abstract class Event {
    public enum Type {
        DISPATCHER__QUIT,
        DRIVER__DATA_RECEIVED,
        MODELS__NOTIFY_RECORD_DEPARTED,
        DRIVER__SEND_DATA, MODEL__NOTIFY_FULL, MODELS__NOTIFY_RECORD_ARRIVED
    }

    public abstract Type getType();
}
