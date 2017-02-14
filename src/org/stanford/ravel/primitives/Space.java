package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.SpaceSymbol;
import org.stanford.ravel.compiler.symbol.Symbol;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 7/29/16.
 */
public class Space extends Primitive {
    private final Map<String, InstantiatedModel> mModels = new LinkedHashMap<>();
    private final Map<String, InstantiatedController> mControllers = new LinkedHashMap<>();
    private final Map<String, View> mViews = new LinkedHashMap<>();
    private final Map<String, InstantiatedInterface> mInterfaces = new LinkedHashMap<>();
    private final SpaceSymbol mSymbol;
    private final SystemAPI mSystemAPI = new SystemAPI(this, this, "system");

    private Platform mPlatform;

    public Space(SpaceSymbol symbol) {
        super(symbol.getName());
        mSymbol = symbol;
    }

    /** add components to the particular space */
    public void add(String ref, InstantiatedModel m) { this.mModels.put(ref, m); }
    public void add(String ref, InstantiatedController c) { this.mControllers.put(ref, c); }
    public void add(String ref, View v) { this.mViews.put(ref,  v); }
    public void add(String ref, InstantiatedInterface s) { this.mInterfaces.put(ref,  s); }

    private InstantiatedModel findModel(Model m) {
        for (InstantiatedModel im : mModels.values()) {
            if (im.getBaseModel() == m)
                return im;
        }
        return null;
    }

    public boolean hasModel(Model m) {
        return findModel(m) != null;
    }

    public Collection<InstantiatedModel> getModels() {
        return Collections.unmodifiableCollection(mModels.values());
    }
    public InstantiatedModel getModel(String modelName) {
        return mModels.get(modelName);
    }
    public Collection<InstantiatedInterface> getInterfaces() {
        return Collections.unmodifiableCollection(mInterfaces.values());
    }
    public InstantiatedInterface getInterface(String sourceName) {
        return mInterfaces.get(sourceName);
    }
    public Collection<InstantiatedController> getControllers() {
        return Collections.unmodifiableCollection(mControllers.values());
    }

    public SystemAPI getSystemAPI() {
        return mSystemAPI;
    }

    public void freezeAll() {
        mSystemAPI.freeze();
        for (InstantiatedModel im : mModels.values())
            im.freeze();
        for (InstantiatedInterface is : mInterfaces.values())
            is.freeze();
    }

    public void setPlatform(Platform build) {
        mPlatform = build;
    }
    public Platform getPlatform() {
        return mPlatform;
    }

    public Symbol getSymbol() {
        return mSymbol;
    }

    @Override
    public String toString() {
        return "Space " + getName();
    }
}
