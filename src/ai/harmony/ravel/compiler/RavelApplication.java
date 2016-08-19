package ai.harmony.ravel.compiler;

import ai.harmony.ravel.compiler.scopes.GlobalScope;
import ai.harmony.ravel.primitives.*;

import java.util.LinkedHashMap;
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

    public void addController(String name, Controller c){
        mControllers.put(name, c);
    }
    public void addSpace(String name, Space s){
        mSpace.put(name, s);
    }
    public void addView(String name, View v){
        mViews.put(name, v);
    }
    public void addVSource(String name, Source s){
        mSource.put(name, s);
    }
    public void addSink(String name, Sink s){
        mSink.put(name, s);
    }
    public void addFlow(String name, Flow f){
        mFlow.put(name, f);
    }


}
