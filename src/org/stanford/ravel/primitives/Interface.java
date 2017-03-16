package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.InterfaceSymbol;
import org.stanford.ravel.compiler.types.Type;

import java.util.*;

/**
 * Created by gcampagn on 1/30/17.
 */
public class Interface extends ConfigurableComponent {
    private final InterfaceSymbol symbol;
    private final Map<String, String> implementation = new HashMap<>();
    private final List<String> events = new ArrayList<>();
    private final Map<String, Model> dependentModels = new HashMap<>();

    public Interface(String name, InterfaceSymbol symbol) {
        super(name);
        this.symbol = symbol;
    }

    public ConcreteInterface instantiate(Space space) {
        return new ConcreteInterface(space, this);
    }

    public Type getType() {
        return symbol.getDefinedType();
    }

    public String getImplementation(String language) {
        return implementation.get(language);
    }

    public void setImplementation(String language, String value) {
        implementation.put(language, value);
    }

    public List<String> getEvents() {
        return Collections.unmodifiableList(events);
    }
    public void addEvent(String event) {
        events.add(event);
    }

    public void dependOnModel(String alias, Model model) {
        assert model != null;
        dependentModels.put(alias, model);
    }
    public Map<String, Model> getModels() {
        return Collections.unmodifiableMap(dependentModels);
    }

    public Map<String, Object> getConfiguration() {
        return getPropertyMap();
    }
}
