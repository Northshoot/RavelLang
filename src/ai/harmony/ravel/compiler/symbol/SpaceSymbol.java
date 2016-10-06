package ai.harmony.ravel.compiler.symbol;


import javafx.util.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lauril on 8/25/16.
 */
public class SpaceSymbol extends ComponentSymbol {

    private Map<String, InstanceSymbol> modelInitMap;
    private Map<String, InstanceSymbol> ctrInitMap;
    private Map<String, ReferenceSymbol> propInitMap;
    private Map<String, ReferenceSymbol> platInitMap;
    private Map<String, ReferenceSymbol> srcInitMap;
    private Map<String, ReferenceSymbol> sinkInitMap;

    public SpaceSymbol(String name) {
        super(name);
        //super.addTypeIndex(RavelParser.SPACE);
        modelInitMap = new LinkedHashMap<>();
        ctrInitMap = new LinkedHashMap<>();
        propInitMap = new LinkedHashMap<>();
        platInitMap = new LinkedHashMap<>();
        srcInitMap = new LinkedHashMap<>();
        sinkInitMap = new LinkedHashMap<>();

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

