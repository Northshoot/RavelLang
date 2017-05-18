package org.stanford.ravel.rrt.events;

/**
 * Created by lauril on 1/31/17.
 */
public abstract class Event {
    public enum Type {
        DISPATCHER__QUIT, // SystemEvent
        DISPATCHER__STOP, // SystemEvent

        SYSTEM_CONNECTED,
        SYSTEM_DISCONNECTED,

        DRIVER__DATA_RECEIVED, // NetworkEvent
        DRIVER__SAVED_DURABLY, // NetworkEvent
        DRIVER__LOAD_FROM_STORAGE, // NetworkEvent
        MODELS__NOTIFY_RECORD_DEPARTED, // NetworkEvent
        MODELS__NOTIFY_RECORD_FAILED_TO_SEND, // NetworkEvent

        GENERIC__RUNNABLE // RunnableEvent
    }

    public abstract Type getType();
}
