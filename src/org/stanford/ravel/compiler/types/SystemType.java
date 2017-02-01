package org.stanford.ravel.compiler.types;

import org.stanford.ravel.primitives.SystemEvent;

/**
 * The type of "system", the thing that has .started(),
 * .stopped(), etc.
 *
 * Created by gcampagn on 1/31/17.
 */
public class SystemType extends ClassType {
    public SystemType() {
        super("SystemAPI");

        for (SystemEvent event : SystemEvent.values()) {
            addEvent(event.name(), new Type[]{}, false);
        }
    }
}
