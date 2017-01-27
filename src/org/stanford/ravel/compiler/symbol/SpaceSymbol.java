package org.stanford.ravel.compiler.symbol;


import org.stanford.ravel.compiler.types.SpaceType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/25/16.
 */
public class SpaceSymbol extends ComponentSymbol {
    private final SpaceType definedType;
    private final Map<String, InstanceSymbol> modelInitMap;
    private final Map<String, InstanceSymbol> ctrInitMap;
    private final Map<String, ReferenceSymbol> propInitMap;
    private final Map<String, ReferenceSymbol> platInitMap;
    private final Map<String, ReferenceSymbol> srcInitMap;
    private final Map<String, ReferenceSymbol> sinkInitMap;

    public SpaceSymbol(String name) {
        super(name);

        definedType = new SpaceType(name);
        modelInitMap = new LinkedHashMap<>();
        ctrInitMap = new LinkedHashMap<>();
        propInitMap = new LinkedHashMap<>();
        platInitMap = new LinkedHashMap<>();
        srcInitMap = new LinkedHashMap<>();
        sinkInitMap = new LinkedHashMap<>();
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

    public Map<String, ReferenceSymbol> getSource() {
        return srcInitMap;
    }

    public void addSource(String name, ReferenceSymbol ls) {
        this.srcInitMap.put(name, ls);
    }

    public Map<String, ReferenceSymbol> getSink() {
        return sinkInitMap;
    }

    public void addSink(String name, ReferenceSymbol ls) {
        this.sinkInitMap.put(name, ls);
    }


}

