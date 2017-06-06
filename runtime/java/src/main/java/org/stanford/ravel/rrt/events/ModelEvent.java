package org.stanford.ravel.rrt.events;

import org.stanford.ravel.rrt.Context;

/**
 * Created by larry on 6/5/17.
 */
public class ModelEvent extends Event {
    private final Event.Type eventType;


    public ModelEvent(Event.Type eventType) {
        this.eventType = eventType;
    }

    @Override
    public Type getType() {
        return this.eventType;
    }
}
