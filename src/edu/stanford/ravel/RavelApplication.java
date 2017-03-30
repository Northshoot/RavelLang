package edu.stanford.ravel;

import edu.stanford.ravel.primitives.*;

import java.util.*;

/**
 * Internal representation of the ravel application
 *
 * a container abstraction for the primitives and components
 * Created by lauril on 8/18/16.
 */
public class RavelApplication  {

    RavelApplication() {
    }

    private final Map<String, Model> mModels = new LinkedHashMap<>();
    public void addModel(String name, Model m){
        mModels.put(name, m);
    }
    public Model getModel(String name) { return mModels.get(name); }
    public List<Model> getModels(){ return new ArrayList<>(mModels.values());}

    private final Map<String, Controller> mControllers = new LinkedHashMap<>();
    public void addController(String name, Controller m){
        mControllers.put(name, m);
    }
    public Controller getController(String name) { return mControllers.get(name); }
    public List<Controller> getControllers(){ return new ArrayList<>(mControllers.values());}

    private final Map<String, Space> mSpace = new LinkedHashMap<>();
    public void addSpace(String name, Space s){
        mSpace.put(name, s);
    }
    public Space getSpace(String name) { return mSpace.get(name); }
    public List<Space> getSpaces(){ return new ArrayList<>(mSpace.values());}

    private final Map<String, View> mViews = new LinkedHashMap<>();
    public void addView(String name, View v) {
        mViews.put(name, v);
    }
    public View getView(String name) { return mViews.get(name); }
    public List<View> getViews(){ return new ArrayList<>(mViews.values());}

    private final Map<String, Interface> mInterfaces = new LinkedHashMap<>();
    public void addInterface(String name, Interface s) {
        mInterfaces.put(name, s);
    }
    public Interface getInterface(String name) { return mInterfaces.get(name); }
    public List<Interface> getInterfaces(){ return new ArrayList<>(mInterfaces.values());}

    private final Set<Flow> mFlow = new HashSet<>();
    public void addFlow(Flow f) {
        mFlow.add(f);
    }
    public Collection<Flow> getFlows() {
        return Collections.unmodifiableCollection(mFlow);
    }

    public String toString() {
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
        if(mInterfaces.size() >0) {
            ret += "\tInterfaces: \n\t";
            for (Map.Entry<String, Interface> entry : mInterfaces.entrySet()) {
                ret += "Interface : " + entry.getKey() + "\n\t" + entry.getValue();
            }
        } else {
            ret+="\t No Interfaces";
        }
        if (mFlow.size() >0) {
            ret += "\tFlows: \n\t";
            for (Flow entry : mFlow) {
                ret += "Flow : " + entry;
            }
        } else {
            ret+="\t No Flow";
        }

        return ret;
    }

}
