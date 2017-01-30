package org.stanford.ravel.compiler.types;

/**
 * The type of an event (a proxy for a function type)
 *
 * Created by gcampagn on 1/30/17.
 */
public class EventType implements Type {
    private final FunctionType fn;
    private final ContextType ctx;
    private final Object key;

    EventType(FunctionType fn, ContextType ctx, Object key) {
        this.fn = fn;
        this.ctx = ctx;
        this.key = key;
    }

    @Override
    public String getName() {
        return "event:" + this.fn.getName();
    }

    public CompoundType getContextType() {
        return ctx;
    }

    /**
     * The event key is a tag that can be applied to an event type by
     * the owner type to more quickly distinguish between different events.
     * For ModelTypes, the event key is a primitives.ModelEvent
     *
     * @return the event key
     */
    public Object getKey() {
        return key;
    }
}
