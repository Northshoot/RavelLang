package org.stanford.ravel.primitives;

import java.util.ArrayList;
import java.util.List;

/**
 * A sink that has been instantiated in a platform.
 * This is an EventComponent (even though it has no event) because it makes it uniform
 * with InstantiatedModel and InstantiatedSource, and makes it easy to retrieve from
 * InstantiatedController.
 *
 * Created by gcampagn on 1/30/17.
 */
public class InstantiatedSink extends BaseEventComponent<InstantiatedSink.NoEvents> {
    enum NoEvents {};

    InstantiatedSink(Space space, Sink sink, String varName) {
        super(space, sink, varName, NoEvents.values());
    }
}
