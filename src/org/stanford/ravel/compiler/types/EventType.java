package org.stanford.ravel.compiler.types;

/**
 * The type of an event (a proxy for a function type)
 *
 * Created by gcampagn on 1/30/17.
 */
public class EventType implements Type {
    private final FunctionType fn;
    private final boolean hasSelf;

    EventType(FunctionType fn, boolean hasSelf) {
        this.fn = fn;
        this.hasSelf = hasSelf;
    }

    @Override
    public String getName() {
        return this.fn.getName();
    }

    public String getEventName() {
        return this.fn.getFunctionName();
    }

    public boolean hasSelf() {
        return hasSelf;
    }

    public Type[] getArgumentTypes() {
        return this.fn.getArgumentTypes();
    }
}
