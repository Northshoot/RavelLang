package org.stanford.ravel.primitives;

/**
 * An EventComponent for system events.
 *
 * There is exactly one of these for each Space.
 *
 * Created by gcampagn on 1/31/17.
 */
public class SystemAPI extends BaseEventComponent {
    public SystemAPI(Space space, Primitive primitive, String varName) {
        super(space, primitive, varName);

        for (SystemEvent event : SystemEvent.values())
            createEvent(event.name());
    }
}
