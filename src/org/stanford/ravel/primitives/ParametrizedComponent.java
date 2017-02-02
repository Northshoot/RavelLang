package org.stanford.ravel.primitives;

import java.util.*;
import java.util.logging.Logger;

/**
 * A component that can be parametrized by passing values from the space.
 *
 * It has parameters, that are passed by the space, and properties, which
 * are properties/configurations from the base primitive, with values set
 * from the constants in the base primitive or forwarded from the parameters.
 *
 * Created by lauril on 10/6/16.
 */
class ParametrizedComponent extends Primitive {
    private static Logger LOGGER = Logger.getLogger(ParametrizedComponent.class.getName());

    private final Map<String, Object> mParameterMap = new LinkedHashMap<>();
    private final Map<String, Object> mPropertyMap = new HashMap<>();

    ParametrizedComponent(String name) {
        super(name);
    }

    public void setParam(String key, Object value) {
        mParameterMap.put(key, value);
    }
    void setManyParam(Map<? extends String, ?> map) {
        mParameterMap.putAll(map);
    }
    public Object getParam(String key) {
        return mParameterMap.get(key);
    }
    Collection<Object> getAllParameters() {
        return mParameterMap.values();
    }

    public boolean isParamSet(String key) {
        return mParameterMap.containsKey(key);
    }

    void setProperty(String name, Object value) {
        mPropertyMap.put(name, value);
    }
    Object getProperty(String name) {
        return mPropertyMap.get(name);
    }
    Map<String, Object> getPropertyMap() {
        return mPropertyMap;
    }
}
