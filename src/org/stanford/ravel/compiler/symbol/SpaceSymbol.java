package org.stanford.ravel.compiler.symbol;


import org.stanford.ravel.compiler.types.SpaceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lauril on 8/25/16.
 */
public class SpaceSymbol extends ComponentSymbol {
    private final SpaceType definedType;
    private final Map<String, InstanceSymbol> modelInitMap;
    private final Map<String, InstanceSymbol> ctrInitMap;
    private final Map<String, InstanceSymbol> ifaceInitMap;
    private final Map<String, ReferenceSymbol> propInitMap;
    private final Map<String, ReferenceSymbol> platInitMap;

    public SpaceSymbol(String name) {
        super(name);

        definedType = new SpaceType(name);
        modelInitMap = new HashMap<>();
        ctrInitMap = new HashMap<>();
        propInitMap = new HashMap<>();
        platInitMap = new HashMap<>();
        ifaceInitMap = new HashMap<>();
    }

    @Override
    public SpaceType getDefinedType() {
        return definedType;
    }

    public Map<String, InstanceSymbol> getModels() {
        return modelInitMap;
    }

    public void addModels(String name, InstanceSymbol ls) {
        this.modelInitMap.put(name, ls);
    }

    public Map<String, InstanceSymbol> getControllers() {
        return ctrInitMap;
    }

    public void addControllers(String name, InstanceSymbol ls) {
        this.ctrInitMap.put(name, ls);
    }

    public Map<String, ReferenceSymbol> getPlatform() {
        return platInitMap;
    }

    public void addPlatform(String name, ReferenceSymbol ls) {
        this.platInitMap.put(name, ls);
    }

    public Map<String, InstanceSymbol> getInterfaces() {
        return ifaceInitMap;
    }

    public void addInterface(String name, InstanceSymbol is) {
        ifaceInitMap.put(name, is);
    }
}

