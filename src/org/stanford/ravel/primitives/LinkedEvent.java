package org.stanford.ravel.primitives;

/**
 * An event on a controller, associated with a specific concrete model
 *
 * Created by gcampagn on 1/26/17.
 */
public class LinkedEvent {
    private final Model model;
    private final Event event;

    LinkedEvent(Model model, Event event) {
        this.model = model;
        this.event = event;
    }
}
