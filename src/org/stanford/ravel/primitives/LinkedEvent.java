package org.stanford.ravel.primitives;

import org.stanford.ravel.analysis.FieldTag;
import org.stanford.ravel.analysis.ModelTag;

import java.util.*;

/**
 * An event on a controller, associated with a specific concrete component
 *
 * Created by gcampagn on 1/26/17.
 */
public class LinkedEvent {
    private final ConcreteControllerInstance controller;
    private final EventComponentInstance component;
    private final EventHandler event;

    private final Map<Integer, Set<Space>> variableCreators = new HashMap<>();
    private final Map<Integer, Set<ModelTag>> variableModelTags = new HashMap<>();
    private final Map<Integer, Set<FieldTag>> variableFieldTags = new HashMap<>();

    LinkedEvent(ConcreteControllerInstance controller, EventComponentInstance component, EventHandler event) {
        this.controller = controller;
        this.component = component;
        this.event = event;
    }

    public EventHandler getHandler() {
        return event;
    }
    public String getVarName() {
        return event.getModelVar().getName();
    }
    public String getEventName() {
        return event.getEventName();
    }

    public EventComponent getComponent() {
        return component.getComponent();
    }
    public EventComponentInstance getComponentInstance() {
        return component;
    }
    public ConcreteController getController() {
        return controller.getComponent();
    }
    public ConcreteControllerInstance getControllerInstance() {
        return controller;
    }

    public boolean addVariableCreator(int var, Space space) {
        Set<Space> spaces = variableCreators.computeIfAbsent(var, (ignored) -> new HashSet<>());
        return spaces.add(space);
    }
    public boolean addVariableModelTag(int var, ModelTag tag) {
        Set<ModelTag> tags = variableModelTags.computeIfAbsent(var, (ignored) -> new HashSet<>());
        assert tags.isEmpty() || !tag.isLocal();
        return tags.add(tag);
    }
    public boolean addVariableFieldTag(int var, FieldTag tag) {
        Set<FieldTag> tags = variableFieldTags.computeIfAbsent(var, (ignored) -> new HashSet<>());
        assert tags.isEmpty() || !tag.isLocal();
        return tags.add(tag);
    }

    public Map<Integer, Set<Space>> getAllVariableCreators() {
        return Collections.unmodifiableMap(variableCreators);
    }
    public Set<Space> getVariableCreators(int var) {
        return Collections.unmodifiableSet(variableCreators.getOrDefault(var, Collections.emptySet()));
    }
    public Map<Integer, Set<ModelTag>> getAllVariableModelTags() {
        return Collections.unmodifiableMap(variableModelTags);
    }
    public Set<ModelTag> getVariableModelTags(int var) {
        return Collections.unmodifiableSet(variableModelTags.getOrDefault(var, Collections.emptySet()));
    }
    public Map<Integer, Set<FieldTag>> getAllVariableFieldTags() {
        return Collections.unmodifiableMap(variableFieldTags);
    }
    public Set<FieldTag> getVariableFieldTags(int var) {
        return Collections.unmodifiableSet(variableFieldTags.getOrDefault(var, Collections.emptySet()));
    }

    public String toString() {
        return "Event " + controller.getComponent().getSpace().getName() + "." + controller.getVarName() + "." + event.getEventName();
    }
}
