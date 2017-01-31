package org.stanford.ravel.primitives;

/**
 * An interface that has been instantiated in a platform.
 *
 * Created by gcampagn on 1/30/17.
 */
public class InstantiatedInterface extends BaseEventComponent {
    private final Interface mIface;

    InstantiatedInterface(Space space, Interface iface, String varName) {
        super(space, iface, varName);
        mIface = iface;

        for (String event : iface.getEvents())
            createEvent(event);
    }

    public Interface getBaseInterface() {
        return mIface;
    }
}
