package org.stanford.ravel.compiler.types;

import org.stanford.ravel.primitives.SystemEvent;

/**
 * The type of "system", the thing that has .started(),
 * .stopped(), etc.
 *
 * Created by gcampagn on 1/31/17.
 */
public class SystemType extends ClassType {
    private SystemType() {
        super("SystemAPI");

        for (SystemEvent event : SystemEvent.values()) {
            addEvent(event.name(), new Type[]{}, false);
        }
        addEvent("connected", new Type[]{IntrinsicTypes.ENDPOINT}, false);
        addEvent("disconnected", new Type[]{IntrinsicTypes.ENDPOINT}, false);

        addMethod("print", new Type[]{PrimitiveType.STR}, PrimitiveType.VOID);
        addMethod("print_number", new Type[]{PrimitiveType.STR, PrimitiveType.INT32}, PrimitiveType.VOID);
    }

    public static final SystemType INSTANCE = new SystemType();
}
