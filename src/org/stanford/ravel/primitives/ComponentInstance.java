package org.stanford.ravel.primitives;

import java.util.*;

/**
 * An instance of a ConcreteInterface or a ConcreteModel.
 *
 * It has parameters, that are passed by the space.
 *
 * Created by lauril on 10/6/16.
 */
public class ComponentInstance<Component extends Primitive> extends Primitive {
    private static int idCounter = 1;

    private final Map<String, Object> mParameterMap = new HashMap<>();
    private final List<Object> mParameterList = new ArrayList<>();
    private final Space mSpace;
    private final String mVarName;
    private final Component mComponent;
    private final int id;

    ComponentInstance(Component component, Space space, String varName) {
        super(component.getName() + "_" + varName);
        mComponent = component;
        mSpace = space;
        mVarName = varName;
        id = idCounter++;
    }

    public int getInstanceId() {
        return id;
    }

    public String getVarName() {
        return mVarName;
    }
    public Component getComponent() {
        return mComponent;
    }

    public void setParam(String key, Object value) {
        assert value != null;
        mParameterMap.put(key, value);
    }
    public Object getParam(String key) {
        return mParameterMap.get(key);
    }

    void freeze(Collection<String> paramOrder) {
        for (String name : paramOrder) {
            mParameterList.add(mParameterMap.get(name));
        }
    }

    public Collection<Object> getAllParameters() {
        return mParameterList;
    }

    public boolean isParamSet(String key) {
        return mParameterMap.containsKey(key);
    }

    @Override
    public String toString() {
        return "Component Instance " + mSpace.getName() + "." + mVarName;
    }
}
