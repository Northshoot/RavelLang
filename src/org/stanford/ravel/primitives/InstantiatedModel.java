package org.stanford.ravel.primitives;

import java.util.*;

/**
 * Created by gcampagn on 1/26/17.
 */
public class InstantiatedModel extends BaseEventComponent<ModelEvent> {
    private final Model mModel;

    InstantiatedModel(Space space, Model model, String varName) {
        super(space, model, varName, ModelEvent.values());
        this.mModel = model;
    }

    public int getSize() {
        return (Integer) getParam("size");
    }

    public boolean isReliable() {
        return (Boolean) getParam("reliable");
    }
    public boolean isDurable() {
        return (Boolean) getParam("durable");
    }

    public Model getBaseModel() {
        return mModel;
    }
}
