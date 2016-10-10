package ai.harmony.ravel.primitives;

import ai.harmony.api.builder.FileObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lauril on 7/29/16.
 */
public class Space extends Primitive{
    private String mTransmitFunction;
    private String mBuildPath;
    private Map<String, Model> mModels = new LinkedHashMap<>();
    private Map<String, Controller> mControllers = new LinkedHashMap<>();
    private Map<String, View> mViews = new LinkedHashMap<>();
    private Map<String, Sink> mSink = new LinkedHashMap<>();
    private Map<String, Source> mSource = new LinkedHashMap<>();
    private Platform mPlatform;


    public Space(String name){
        super(name);
    }

    /** add components to the particular space */
    public void add(String ref, Model m) { this.mModels.put(ref, m); }
    public void add(String ref, Controller c) { this.mControllers.put(ref, c); }
    public void add(String ref, View v) { this.mViews.put(ref,  v); }
    public void add(String ref, Sink s) { this.mSink.put(ref,  s); }
    public void add(String ref, Source v) { this.mSource.put(ref,  v); }


    public String getTransmitFunction(){
        return "random_char_update";
    }

    public String getService(){
        return "ravel_service";
    }

    public Model resolveModel(String name){
        if(name.startsWith("tier.models.")){
            return mModels.get(name.replace("tier.models.",""));
        } else {
            throw new RuntimeException("Cannot resolve model: " + name);
        }
    }
    public List<Model> getModels(){
        List<Model> lst = new ArrayList<>();
        lst.addAll(mModels.values());
        return lst;
    }

    public List<Controller> getControllers(){
        List<Controller> lst = new ArrayList<>();
        lst.addAll(mControllers.values());
        return lst;
    }
    public void add(Platform build) {
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

    public List<FileObject> buildAll() {
        List<FileObject> mFiles = new ArrayList<>();
        mFiles.addAll(mPlatform.buildPlatform(this));
        mFiles.addAll(mPlatform.buildLanguage(this));
        return mFiles;
    }

    public void buildLanguage() {

    }
    public String getBuildPath(){
        return this.mBuildPath;
    }
    public String setBuildPath(String p){
        return this.mBuildPath=p+this.mName;
    }
    public void buildPlatform() {
    }

    /**
     * get time current
     * TODO: shoudl be moved to a builder class that is passed to the template with all the preferences
     * @return
     */
    public String getTimeDate(){
        Date t = Calendar.getInstance().getTime();
        return new SimpleDateFormat("HH:mm:ss MM/dd/yyyy").format(t).toString();
    }


}
