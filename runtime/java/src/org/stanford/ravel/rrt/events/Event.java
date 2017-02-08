package org.stanford.ravel.rrt.events;

/**
 * Created by lauril on 1/31/17.
 */
public abstract class Event {
    public enum Type {
        DISPATCHER__QUIT,
        DISPATCHER__STOP,

        DRIVER__DATA_RECEIVED,

        MODELS__NOTIFY_RECORD_DEPARTED,
        MODELS__NOTIFY_RECORD_ARRIVED,

        GENERIC__RUNNABLE
    }

    public abstract Type getType();
}
