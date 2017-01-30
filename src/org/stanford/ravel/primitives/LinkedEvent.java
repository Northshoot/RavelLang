package org.stanford.ravel.primitives;

/**
 * An event on a controller, associated with a specific concrete component
 *
 * Created by gcampagn on 1/26/17.
 */
public class LinkedEvent {
    private final InstantiatedController controller;
    private final EventComponent component;
    private final EventHandler event;

    LinkedEvent(InstantiatedController controller, EventComponent component, EventHandler event) {
        this.controller = controller;
        this.component = component;
        this.event = event;
    }

    public EventHandler getHandler() {
        return event;
    }
    public EventComponent getComponent() {
        return component;
    }
    public InstantiatedController getController() {
        return controller;
    }
}
