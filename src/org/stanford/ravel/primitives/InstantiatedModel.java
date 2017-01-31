package org.stanford.ravel.primitives;

import java.util.*;

/**
 * Created by gcampagn on 1/26/17.
 */
public class InstantiatedModel extends BaseEventComponent {
    public final static int DEFAULT_MODEL_SIZE = 10;

    private final Model mModel;
    private final Map<String, Object> mPropertyMap = new HashMap<>();

    InstantiatedModel(Space space, Model model, String varName) {
        super(space, model, varName);
        this.mModel = model;

        for (ModelEvent e : ModelEvent.values())
            createEvent(e.name());

        // set defaults
        mPropertyMap.put("records", DEFAULT_MODEL_SIZE);
        mPropertyMap.put("reliable", false);
        mPropertyMap.put("durable", false);
    }

    void setProperty(String name, Object value) {
        mPropertyMap.put(name, value);
    }
    private Object getProperty(String name) {
        return mPropertyMap.get(name);
    }

    public int getSize() {
        return (Integer) getProperty("records");
    }
    public boolean isReliable() {
        return (Boolean) getProperty("reliable");
    }
    public boolean isDurable() {
        return (Boolean) getProperty("durable");
    }

    public Model getBaseModel() {
        return mModel;
    }
}
