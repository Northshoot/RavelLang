package edu.stanford.ravel.compiler.types;

import edu.stanford.ravel.primitives.SystemEvent;

/**
 * The type of "system", the thing that has .started(),
 * .stopped(), etc.
 *
 * Created by gcampagn on 1/31/17.
 */
public class SystemType extends ClassType {
    private SystemType() {
        super("SystemAPI");
        //TODO: add new methods and events here
        for (SystemEvent event : SystemEvent.values()) {
            addEvent(event.name(), new Type[]{}, false);
        }
        addEvent("connected", new Type[]{IntrinsicTypes.ENDPOINT}, false);
        addEvent("disconnected", new Type[]{IntrinsicTypes.ENDPOINT}, false);

        addMethod("print", new Type[]{PrimitiveType.STR}, PrimitiveType.VOID);
        addMethod("deviceID", new Type[]{}, PrimitiveType.INT32);
        addMethod("print_number", new Type[]{PrimitiveType.STR, PrimitiveType.INT32}, PrimitiveType.VOID);
    }

    public static final SystemType INSTANCE = new SystemType();
}
