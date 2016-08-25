package ai.harmony.ravel;

import ai.harmony.ravel.compiler.scopes.GlobalScope;
import ai.harmony.ravel.primitives.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lauril on 8/18/16.
 */
public class RavelApplication  {
    Map<String, Model> mModels = new LinkedHashMap<>();
    Map<String, Controller> mControllers = new LinkedHashMap<>();
    Map<String, Space> mSpace = new LinkedHashMap<>();
    Map<String, View> mViews = new LinkedHashMap<>();
    Map<String, Source> mSource = new LinkedHashMap<>();
    Map<String, Sink> mSink = new LinkedHashMap<>();
    Map<String, Flow> mFlow = new LinkedHashMap<>();



    public RavelApplication() {

    }

    public void addModel(String name, Model m){
        mModels.put(name, m);
    }
    public Model getModel() { return (Model) getFirst(mModels); }
    public List<Model> getModels(){ return new ArrayList<>(mModels.values());}

    public void addController(String name, Controller c){
        mControllers.put(name, c);
    }
    public void addSpace(String name, Space s){
        mSpace.put(name, s);
    }
    public void addView(String name, View v){
        mViews.put(name, v);
    }
    public void addSource(String name, Source s){
        mSource.put(name, s);
    }
    public void addSink(String name, Sink s){
        mSink.put(name, s);
    }
    public void addFlow(String name, Flow f){
        mFlow.put(name, f);
    }

    private Object getFirst(Map m){
        Object firstKey = m.keySet().toArray()[0];
        return m.get(firstKey);
    }
    public String toString(){
        String ret ="Ravel applications: \n";
        if(mModels.size() >0) {
            ret += "\tModels: \n\t";
            for (Map.Entry<String, Model> entry : mModels.entrySet()) {
                ret += "Model : " + entry.getKey() + "\n\t" + entry.getValue();
            }
        } else {
            ret+="\t No models";
        }
        ret+="\n/**********************************************************/\n";
        if(mControllers.size() >0) {
            ret += "\tControllers: \n\t";
            for (Map.Entry<String, Controller> entry : mControllers.entrySet()) {
                ret += "Key : " + entry.getKey() + "\n\t" + entry.getValue();
            }
        } else {
            ret+="\t No Controllers";
        }
        ret+="\n/**********************************************************/\n";
        if(mSpace.size() >0) {
            ret += "\tSpace: \n\t";
            for (Map.Entry<String, Space> entry : mSpace.entrySet()) {
                ret += "Space : " + entry.getKey() + "\n\t" + entry.getValue();
            }
        } else {
            ret+="\t No Spaces";
        }
        ret+="\n/**********************************************************/\n";
        if(mViews.size() >0) {
            ret += "\tViews: \n\t";
            for (Map.Entry<String, View> entry : mViews.entrySet()) {
                ret += "View : " + entry.getKey() + "\n\t" + entry.getValue();
            }
        } else {
            ret+="\t No Views";
        }
        ret+="\n/**********************************************************/\n";
        if(mSource.size() >0) {
            ret += "\tSource: \n\t";
            for (Map.Entry<String, Source> entry : mSource.entrySet()) {
                ret += "Source : " + entry.getKey() + "\n\t" + entry.getValue();
            }
        } else {
            ret+="\t No models";
        }
        ret+="\n/**********************************************************/\n";
        if(mSink.size() >0) {
            ret += "\tSinks: \n\t";
            for (Map.Entry<String, Sink> entry : mSink.entrySet()) {
                ret += "Sink : " + entry.getKey() + "\n\t" + entry.getValue();
            }
        } else {
            ret+="\t No Sinks";
        }
        ret+="\n/**********************************************************/\n";
        if(mFlow.size() >0) {
            ret += "\tModels: \n\t";
            for (Map.Entry<String, Flow> entry : mFlow.entrySet()) {
                ret += "Flow : " + entry.getKey() + "\n\t" + entry.getValue();
            }
        } else {
            ret+="\t No Flow";
        }

        return ret;
    }
}
