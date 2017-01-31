package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.InterfaceSymbol;
import org.stanford.ravel.compiler.types.Type;

import java.util.*;

/**
 * Created by gcampagn on 1/30/17.
 */
public class Interface extends Primitive {
    private final InterfaceSymbol symbol;
    private final Map<String, String> implementation = new HashMap<>();
    private final List<String> events = new ArrayList<>();

    public Interface(String name, InterfaceSymbol symbol) {
        super(name);
        this.symbol = symbol;
    }

    public InstantiatedInterface instantiate(Space space, Map<String, Object> parameters, String varName) {
        InstantiatedInterface instantiated = new InstantiatedInterface(space, this, varName);
        instantiated.setManyParam(parameters);
        // TODO: check types of parameters
        // TODO: check that all parameters are set
        return instantiated;
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
}
