package org.stanford.ravel.rrt.events;

/**
 * A generic event that runs a Runnable at the next iteration
 * of the event queue
 *
 * Created by gcampagn on 2/8/17.
 */
public abstract class RunnableEvent extends Event implements Runnable {
    @Override
    public Type getType() {
        return Type.GENERIC__RUNNABLE;
    }
}
