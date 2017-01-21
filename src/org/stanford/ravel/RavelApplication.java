package org.stanford.ravel;

import org.stanford.ravel.primitives.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Internal representation of the ravel application
 *
 * a container abstraction for the primitives and components
 * Created by lauril on 8/18/16.
 */
public class RavelApplication  {

    public RavelApplication() {

    }

    Map<String, Model> mModels = new LinkedHashMap<>();
    public void addModel(String name, Model m){
        mModels.put(name, m);
    }
    public Model getModel() { return (Model) getFirst(mModels); }
    public Model getModel(String name) { return mModels.get(name); }
    public List<Model> getModels(){ return new ArrayList<>(mModels.values());}

    Map<String, Controller> mControllers = new LinkedHashMap<>();
    public void addController(String name, Controller m){
        mControllers.put(name, m);
    }
    public Controller getController(String name) { return mControllers.get(name); }
    public List<Controller> getControllers(){ return new ArrayList<>(mControllers.values());}

    Map<String, Space> mSpace = new LinkedHashMap<>();
    public void addSpace(String name, Space s){
        mSpace.put(name, s);
    }
    public Space getSpace(String name) { return mSpace.get(name); }
    public List<Space> getSpaces(){ return new ArrayList<>(mSpace.values());}

    Map<String, View> mViews = new LinkedHashMap<>();
    public void addView(String name, View v){
        mViews.put(name, v);
    }
    public View getView(String name) { return mViews.get(name); }
    public List<View> getViews(){ return new ArrayList<>(mViews.values());}

    Map<String, Source> mSource = new LinkedHashMap<>();
    public void addSource(String name, Source s){
        mSource.put(name, s);
    }
    public Source getSource(String name) { return mSource.get(name); }
    public List<Source> getSource(){ return new ArrayList<>(mSource.values());}

    Map<String, Sink> mSink = new LinkedHashMap<>();
    public void addSink(String name, Sink s){
        mSink.put(name, s);
    }
    public Sink getSink(String name) { return mSink.get(name); }
    public List<Sink> getSinks(){ return new ArrayList<>(mSink.values());}

    Map<String, Flow> mFlow = new LinkedHashMap<>();
    public void addFlow(String name, Flow f){
        mFlow.put(name, f);
    }
    public Flow getFlow(String name) { return mFlow.get(name); }
    public List<Flow> getFlows(){ return new ArrayList<>(mFlow.values());}

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
