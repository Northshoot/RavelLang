package org.stanford.ravel.primitives;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 10/6/16.
 */
class ParametrizedComponent extends Primitive {
    private static Logger LOGGER = Logger.getLogger(ParametrizedComponent.class.getName());

    private Map<String, Object> mParameterMap = new LinkedHashMap<>();
    ParametrizedComponent(String name, String internalName) {
        super(name, internalName);
    }

    public void setParam(String key, Object value){
        mParameterMap.put(key, value);
    }

    void setManyParam(Map<? extends String, ?> map) {
        mParameterMap.putAll(map);
    }
    public Object getParam(String key) {
        return mParameterMap.get(key);
    }

    public boolean isParamSet(String key) {
        return mParameterMap.containsKey(key);
    }
}
