package org.stanford.ravel.compiler.types;

import org.stanford.ravel.primitives.SourceEvent;

/**
 * The type of a source
 *
 * Created by gcampagn on 1/30/17.
 */
public class SourceType extends ClassType {
    private final ContextType ctxType;

    public SourceType(String name) {
        super(name);

        ctxType = new ContextType(this);

        for (SourceEvent event : SourceEvent.values())
            addEvent(event.name(), new Type[]{}, PrimitiveType.VOID, ctxType, event);
    }
}
