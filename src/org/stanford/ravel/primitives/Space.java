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
    private final Map<String, Sink> mSink = new LinkedHashMap<>();
    private final Map<String, Source> mSource = new LinkedHashMap<>();
    private Platform mPlatform;

    public Space(String name) {
        super(name);
    }

    /** add components to the particular space */
    public void add(String ref, InstantiatedModel m) { this.mModels.put(ref, m); }
    public void add(String ref, InstantiatedController c) { this.mControllers.put(ref, c); }
    public void add(String ref, View v) { this.mViews.put(ref,  v); }
    public void add(String ref, Sink s) { this.mSink.put(ref,  s); }
    public void add(String ref, Source v) { this.mSource.put(ref,  v); }


    public String getTransmitFunction(){
        return "random_char_update";
    }

    public String getService(){
        return "ravel_service";
    }

    public List<InstantiatedModel> getModels() {
        List<InstantiatedModel> lst = new ArrayList<>();
        lst.addAll(mModels.values());
        return lst;
    }
    public InstantiatedModel getModel(String modelName) {
        return mModels.get(modelName);
    }

    public List<InstantiatedController> getControllers() {
        List<InstantiatedController> lst = new ArrayList<>();
        lst.addAll(mControllers.values());
        return lst;
    }
    public void setPlatform(Platform build) {
        mPlatform = build;
    }

    public void addSource(Source s){
        this.mSource.put(s.getSinkIdentifier(), s);
    }

    public Source getSource(String name){
        return mSource.get(name);
    }
    public List<Source> getSources(){
        List<Source> lst = new ArrayList<>();
        lst.addAll(mSource.values());
        return lst;
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
