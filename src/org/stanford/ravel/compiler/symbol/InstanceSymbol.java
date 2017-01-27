package org.stanford.ravel.compiler.symbol;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.types.Type;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/26/16.
 */
public class InstanceSymbol extends BaseSymbol implements TypedSymbol {

    private String identifier;
    private String instance_name;
    private Map<String, Object>  parameterMap;

    public InstanceSymbol(String name, String instanceName) {
        super(name);
        parameterMap = new LinkedHashMap<>();
        identifier = name;
        instance_name = instanceName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getInstanceName() {
        return instance_name;
    }

    public void addParameter(String key, Object val) {
        parameterMap.put(key, val);
    }
    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }
}