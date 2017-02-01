package org.stanford.ravel.primitives;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lauril on 7/29/16.
 */
public class Space extends Primitive {
    private final Map<String, InstantiatedModel> mModels = new LinkedHashMap<>();
    private final Map<String, InstantiatedController> mControllers = new LinkedHashMap<>();
    private final Map<String, View> mViews = new LinkedHashMap<>();
    private final Map<String, InstantiatedInterface> mInterfaces = new LinkedHashMap<>();
    private final List<Flow> mInFlows = new ArrayList<>();
    private final List<Flow> mOutFlows = new ArrayList<>();
    private final SystemAPI mSystemAPI = new SystemAPI(this, this, "system");

    private Platform mPlatform;

    public Space(String name) {
        super(name);
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

    /**
     * Add a flow directed to a controller in this space
     * @param f the flow
     */
    public void addInboundFlow(Flow f) {
        assert f.getSink().getSpace() == this;
        mInFlows.add(f);
        InstantiatedModel im = findModel(f.getModel());
        assert im != null;
        if (f.getSource().getSpace() != this)
            im.addStreamingSource(f.getSource().getSpace());
    }

    /**
     * Add a flow departing from a controller from this space
     * @param f the flow
     */
    public void addOutboundFlow(Flow f) {
        assert f.getSource().getSpace() == this;
        mOutFlows.add(f);
        InstantiatedModel im = findModel(f.getModel());
        assert im != null;
        if (f.getSink().getSpace() != this)
            im.addStreamingSink(f.getSink().getSpace());
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

    /**
     * get time current
     * TODO: should be moved to a builder class that is passed to the template with all the preferences
     * @return
     */
    public String getTimeDate(){
        Date t = Calendar.getInstance().getTime();
        return new SimpleDateFormat("HH:mm:ss MM/dd/yyyy").format(t).toString();
    }


}
