package org.stanford.ravel.primitives;

import java.util.*;

/**
 * Created by gcampagn on 1/26/17.
 */
public class InstantiatedModel extends BaseEventComponent {
    public final static int DEFAULT_MODEL_SIZE = 10;

    private final Model mModel;
    private final Set<Space> mStreamingSinks = new HashSet<>();
    private final Set<Space> mStreamingSources = new HashSet<>();

    InstantiatedModel(Space space, Model model, String varName) {
        super(space, model, varName);
        this.mModel = model;

        for (ModelEvent e : ModelEvent.values())
            createEvent(e.name());

        // set defaults
        setProperty("records", DEFAULT_MODEL_SIZE);
        setProperty("reliable", false);
        setProperty("durable", false);
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

    public void addStreamingSink(Space target) {
        assert mModel.getModelType() != Model.Type.LOCAL;
        assert target != getSpace();
        mStreamingSinks.add(target);
    }
    public Collection<Space> getStreamingSinks() {
        return Collections.unmodifiableCollection(mStreamingSinks);
    }

    public void addStreamingSource(Space target) {
        assert mModel.getModelType() != Model.Type.LOCAL;
        assert target != getSpace();
        mStreamingSources.add(target);
    }
    public Collection<Space> getStreamingSources() {
        return Collections.unmodifiableCollection(mStreamingSources);
    }
}
