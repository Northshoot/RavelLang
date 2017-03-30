package edu.stanford.ravel.compiler.symbol;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/26/16.
 */
public class InstanceSymbol extends BaseSymbol implements TypedSymbol {
    private final ComponentSymbol referredSymbol;
    private final Map<String, Object>  parameterMap = new LinkedHashMap<>();

    public InstanceSymbol(String name, ComponentSymbol referredSymbol) {
        super(name);
        this.referredSymbol = referredSymbol;
    }

    public String getInstanceName() {
        return referredSymbol.getName();
    }

    public void addParameter(String key, Object val) {
        parameterMap.put(key, val);
    }
    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }
}