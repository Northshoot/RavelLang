package org.stanford.ravel.primitives;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lauril on 7/29/16.
 */
public class Space extends Primitive {
    private String mTransmitFunction;
    private final Map<String, InstantiatedModel> mModels = new LinkedHashMap<>();
    private final Map<String, InstantiatedController> mControllers = new LinkedHashMap<>();
    private final Map<String, View> mViews = new LinkedHashMap<>();
    private final Map<String, InstantiatedSink> mSink = new LinkedHashMap<>();
    private final Map<String, InstantiatedSource> mSource = new LinkedHashMap<>();
    private Platform mPlatform;

    public Space(String name) {
        super(name);
    }

    /** add components to the particular space */
    public void add(String ref, InstantiatedModel m) { this.mModels.put(ref, m); }
    public void add(String ref, InstantiatedController c) { this.mControllers.put(ref, c); }
    public void add(String ref, View v) { this.mViews.put(ref,  v); }
    public void add(String ref, InstantiatedSink s) { this.mSink.put(ref,  s); }
    public void add(String ref, InstantiatedSource v) { this.mSource.put(ref,  v); }

    public Collection<InstantiatedModel> getModels() {
        return Collections.unmodifiableCollection(mModels.values());
    }
    public InstantiatedModel getModel(String modelName) {
        return mModels.get(modelName);
    }
    public Collection<InstantiatedSource> getSources() {
        return Collections.unmodifiableCollection(mSource.values());
    }
    public InstantiatedSource getSource(String sourceName) {
        return mSource.get(sourceName);
    }
    public Collection<InstantiatedSink> getSinks() {
        return Collections.unmodifiableCollection(mSink.values());
    }
    public InstantiatedSink getSink(String sourceName) {
        return mSink.get(sourceName);
    }
    public Collection<InstantiatedController> getControllers() {
        return Collections.unmodifiableCollection(mControllers.values());
    }

    public void freezeAll() {
        for (InstantiatedModel im : mModels.values())
            im.freeze();
        for (InstantiatedSource is : mSource.values())
            is.freeze();
        for (InstantiatedSink is : mSink.values())
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
