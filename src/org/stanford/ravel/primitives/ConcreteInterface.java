package org.stanford.ravel.primitives;

import java.util.Map;

/**
 * An interface that has been instantiated in a platform.
 *
 * Created by gcampagn on 1/30/17.
 */
public class ConcreteInterface extends BaseEventComponent<Interface> {
    ConcreteInterface(Space space, Interface iface) {
        super(space, iface);

        for (String event : iface.getEvents())
            createEvent(event);
    }

    public Interface getBaseInterface() {
        return getBasePrimitive();
    }

    public Map<String, Object> getConfiguration() {
        return getBaseInterface().getPropertyMap();
    }

    @Override
    public String toString() {
        return "Concrete Interface " + getSpace().getName() + "." + getName();
    }
}
