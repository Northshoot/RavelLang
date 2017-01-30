package org.stanford.ravel.primitives;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcampagn on 1/26/17.
 */
public class InstantiatedModel extends ParametrizedComponent {
    private final Space mSpace;
    private final Model mModel;
    private final String mVarName;

    private final List<LinkedEvent> mEvents = new ArrayList<>();

    InstantiatedModel(Space space, Model model, String varName) {
        super(model.getName(), model.getName() + "Model");
        mSpace = space;
        mModel = model;
        mVarName = varName;
    }

    public String getVarName() {
        return mVarName;
    }

    public int getSize() {
        return (Integer) getParam("size");
    }

    public Model getBaseModel() {
        return mModel;
    }
}
