package org.stanford.ravel.primitives;

import org.stanford.ravel.analysis.security.Key;
import org.stanford.ravel.analysis.security.SecurityPrimitive;
import org.stanford.ravel.compiler.symbol.SpaceSymbol;
import org.stanford.ravel.compiler.symbol.Symbol;

import java.util.*;

/**
 * Created by lauril on 7/29/16.
 */
public class Space extends Primitive {
    private static int idCounter = 1;

    private final Set<ConcreteModel> mModels = new HashSet<>();
    private final Map<String, ConcreteModelInstance> mModelInstances = new HashMap<>();
    private final Set<ConcreteController> mControllers = new HashSet<>();
    private final Map<String, ConcreteControllerInstance> mControllerInstances = new HashMap<>();
    private final Map<String, View> mViews = new HashMap<>();
    private final Set<ConcreteInterface> mInterfaces = new HashSet<>();
    private final Map<String, ConcreteInterfaceInstance> mInterfaceInstances = new HashMap<>();
    private final SpaceSymbol mSymbol;
    private final SystemAPIInstance mSystemAPI = new SystemAPIInstance(new SystemAPI(this, this), "system");
    private final int id;

    private Platform mPlatform;

    public Space(SpaceSymbol symbol) {
        super(symbol.getName());

        id = idCounter++;
        mSymbol = symbol;
    }

    public int getId() {
        return id;
    }

    /** add components to the particular space */
    public void add(String ref, View v) { this.mViews.put(ref,  v); }

    public void add(String ref, ConcreteModelInstance m) {
        this.mModels.add(m.getComponent());
        this.mModelInstances.put(ref, m);
    }
    public void add(String ref, ConcreteControllerInstance c) {
        this.mControllers.add(c.getComponent());
        this.mControllerInstances.put(ref, c);
    }
    public void add(String ref, ConcreteInterfaceInstance s) {
        this.mInterfaces.add(s.getComponent());
        this.mInterfaceInstances.put(ref, s);
    }

    private ConcreteModel findModel(Model m) {
        for (ConcreteModel im : mModels) {
            if (im.getBaseModel() == m)
                return im;
        }
        return null;
    }
    public ConcreteInterface findInterface(Interface iface) {
        for (ConcreteInterface iiface : mInterfaces) {
            if (iiface.getBaseInterface() == iface)
                return iiface;
        }
        return null;
    }
    public ConcreteController findController(Controller ctr) {
        for (ConcreteController ictr : mControllers) {
            if (ictr.getController() == ctr)
                return ictr;
        }
        return null;
    }

    public boolean hasModel(Model m) {
        return findModel(m) != null;
    }
    public boolean hasInterface(Interface iface) {
        return findInterface(iface) != null;
    }

    public Collection<ConcreteModel> getModels() {
        return Collections.unmodifiableCollection(mModels);
    }
    public ConcreteModelInstance getModel(String modelName) {
        return mModelInstances.get(modelName);
    }
    public Collection<ConcreteModelInstance> getModelInstances() {
        return Collections.unmodifiableCollection(mModelInstances.values());
    }

    public Collection<ConcreteInterface> getInterfaces() {
        return Collections.unmodifiableCollection(mInterfaces);
    }
    public ConcreteInterfaceInstance getInterface(String sourceName) {
        return mInterfaceInstances.get(sourceName);
    }

    public Collection<ConcreteInterfaceInstance> getInterfaceInstances() {
        return Collections.unmodifiableCollection(mInterfaceInstances.values());
    }

    public Collection<ConcreteController> getControllers() {
        return Collections.unmodifiableCollection(mControllers);
    }
    public Collection<ConcreteControllerInstance> getControllerInstances() {
        return Collections.unmodifiableCollection(mControllerInstances.values());
    }

    public SystemAPIInstance getSystemAPI() {
        return mSystemAPI;
    }

    public void freezeAll() {
        // freeze the components
        mSystemAPI.getComponent().freeze();
        for (ConcreteModel im : mModels)
            im.freeze();
        for (ConcreteInterface is : mInterfaces)
            is.freeze();

        // freeze the instances
        for (ConcreteInterfaceInstance inst : mInterfaceInstances.values())
            inst.freeze();
        for (ConcreteModelInstance inst : mModelInstances.values())
            inst.freeze();
        for (ConcreteControllerInstance inst : mControllerInstances.values())
            inst.freeze();
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
        return "Space #" + id + ": " + getName();
    }

    public void addSecurityOperation(ModelField field, Space target, Flow flow, Key key, SecurityPrimitive primitive, int offset) {
        ConcreteModel im = findModel(field.getModel());
        assert im != null;
        im.addSecurityOperation(field, target, flow, key, primitive, offset);
    }
}
