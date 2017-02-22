package org.stanford.ravel.primitives;

/**
 * An interface that has been instantiated in a platform.
 *
 * Created by gcampagn on 1/30/17.
 */
public class ConcreteInterface extends BaseEventComponent {
    private final Interface mIface;

    ConcreteInterface(Space space, Interface iface) {
        super(space, iface);
        mIface = iface;

        for (String event : iface.getEvents())
            createEvent(event);
    }

    public Interface getBaseInterface() {
        return mIface;
    }

    @Override
    public String toString() {
        return "Concrete Interface " + getSpace().getName() + "." + getName();
    }
}
