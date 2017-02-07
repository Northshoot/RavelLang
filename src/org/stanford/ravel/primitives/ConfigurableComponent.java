package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A base class for interfaces and model, because they both
 * have properties/configuration aspects that can be constants
 * or references to variables set by the space during instantiation.
 *
 * Created by gcampagn on 2/1/17.
 */
public class ConfigurableComponent extends Primitive {
    private final Map<String, Type> mInterface = new HashMap<>();
    private final Map<String, Object> mConstantProperties = new HashMap<>();
    private final Map<String, String> mRefProperties = new HashMap<>();

    ConfigurableComponent(String name) {
        super(name);
    }

    public void addParameter(String name, Type type) {
        mInterface.put(name, type);
    }

    public Type getParameterType(String name) {
        return mInterface.get(name);
    }

    public boolean hasParameter(String pname) {
        return mInterface.containsKey(pname);
    }

    public Collection<String> getParameterNames() {
        return mInterface.keySet();
    }

    public void applyProperties(ParametrizedComponent instance) {
        for (Map.Entry<String, Object> entry : mConstantProperties.entrySet()) {
            instance.setProperty(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : mRefProperties.entrySet()) {
            instance.setProperty(entry.getKey(), instance.getParam(entry.getValue()));
        }
    }

    public void addConstantProperty(String name, Object value) {
        mConstantProperties.put(name, value);
    }
    public void addReferenceProperty(String name, String ref) {
        mRefProperties.put(name, ref);
    }
}