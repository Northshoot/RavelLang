package org.stanford.ravel.primitives;

/**
 * An event on a controller, associated with a specific concrete model
 *
 * Created by gcampagn on 1/26/17.
 */
public class LinkedEvent {
    private final InstantiatedController controller;
    private final InstantiatedModel model;
    private final EventHandler event;

    LinkedEvent(InstantiatedController controller, InstantiatedModel model, EventHandler event) {
        this.controller = controller;
        this.model = model;
        this.event = event;
    }

    public EventHandler getHandler() {
        return event;
    }
    public InstantiatedModel getModel() {
        return model;
    }
    public InstantiatedController getController() {
        return controller;
    }
}
