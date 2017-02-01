package org.stanford.ravel.rrt;

/**
 * Created by lauril on 1/31/17.
 */
public class SystemEvent extends Event {
    private final Type type;

    public SystemEvent(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return null;
    }
}
