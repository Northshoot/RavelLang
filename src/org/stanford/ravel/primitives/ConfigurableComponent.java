package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;

import java.util.*;

/**
 * A base class for interfaces and model, because they both
 * have properties/configuration aspects that can be constants
 * or references to variables set by the space during instantiation.
 *
 * Created by gcampagn on 2/1/17.
 */
public class ConfigurableComponent extends Primitive {
    private final Map<String, Type> mParameterTypes = new HashMap<>();
    private final List<String> mParameterNames = new ArrayList<>();
    private final List<VariableSymbol> mParameters = new ArrayList<>();

    private final Map<String, Object> mProperties = new HashMap<>();

    ConfigurableComponent(String name) {
        super(name);
    }

    public void addParameter(VariableSymbol sym) {
        mParameterTypes.put(sym.getName(), sym.getType());
        mParameterNames.add(sym.getName());
        mParameters.add(sym);
    }
    public Type getParameterType(String name) {
        return mParameterTypes.get(name);
    }
    public boolean hasParameter(String pname) {
        return mParameterTypes.containsKey(pname);
    }
    public Collection<String> getParameterNames() {
        return Collections.unmodifiableCollection(mParameterNames);
    }
    public Collection<VariableSymbol> getParameters() {
        return Collections.unmodifiableCollection(mParameters);
    }

    public void setConstantProperty(String name, Object value) {
        mProperties.put(name, value);
    }
    public void setReferenceProperty(String name, String ref) {
        mProperties.put(name, new Reference(ref));
    }
    public Object getProperty(String name) {
        return mProperties.get(name);
    }
}
