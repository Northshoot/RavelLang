package org.stanford.ravel.primitives;

import java.util.*;

/**
 * An event on a controller, associated with a specific concrete component
 *
 * Created by gcampagn on 1/26/17.
 */
public class LinkedEvent {
    private final InstantiatedController controller;
    private final EventComponent component;
    private final EventHandler event;
    private final Map<Metadata, Map<Integer, Object>> variableMetas = new EnumMap<>(Metadata.class);

    private final Map<Integer, Set<InstantiatedController>> variableCreators = new HashMap<>();

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

    public Map<Integer, Object> getMeta(Metadata key) {
        return variableMetas.computeIfAbsent(key, (ignored) -> new HashMap<>());
    }

    public boolean addVariableCreator(int var, InstantiatedController ic) {
        Set<InstantiatedController> ctrls = variableCreators.computeIfAbsent(var, (ignored) -> new HashSet<>());
        return ctrls.add(ic);
    }
}
