package ai.harmony.ravel;

import ai.harmony.ravel.primitives.*;

import javax.json.*;
import java.util.*;

/**
 * Internal representation of the ravel application
 *
 * a container abstraction for the primitives and components
 * Created by lauril on 8/18/16.
 */
public class RavelApplication  {

    RavelApplication(String name) {
        mName = name;
    }

    private String mName;

    public String getmName(){ return this.mName;}

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

    /**
     * creates are json
     * @return
     */
    public JsonObject toJsonString(){
        JsonObjectBuilder jsonApp = Json.createObjectBuilder();
        jsonApp.add("app.name", getmName());
        jsonApp.add("app.version", RavelProperties.getInstance().getVersion());
        jsonApp.add("app.id", RavelProperties.getInstance().getID());
        jsonApp.add("app.org", RavelProperties.getInstance().getOrg());
        jsonApp.add("compiler.version", RavelProperties.getInstance().getCompilerVersion());



        if(mModels.size() >0) {
            JsonArrayBuilder modelArray = Json.createArrayBuilder();
            for (Map.Entry<String, Model> entry : mModels.entrySet()) {
                JsonObjectBuilder jsonModel = Json.createObjectBuilder();
                jsonModel.add("name", entry.getValue().getName());
                jsonModel.add("type", entry.getValue().getModelType().toString());
                //Add flows
                JsonObjectBuilder jsonModelFlows = Json.createObjectBuilder();
                for(Flow flow: entry.getValue().getFlows()) {
                    jsonModelFlows.add("source", flow.getSource().getName());
                    jsonModelFlows.add("sink", flow.getSink().getName());
                }
                jsonModel.add("flows", jsonModelFlows);
                //array of model fields
                JsonArrayBuilder fieldsArray = Json.createArrayBuilder();
                for( ModelField field : entry.getValue().getFields()){
                    JsonObjectBuilder jsonModelFields = Json.createObjectBuilder();
                    jsonModelFields.add("name", field.getName());
                    jsonModelFields.add("type", field.getType().getName().toLowerCase());
                    fieldsArray.add(jsonModelFields);
                }
                jsonModel.add("fields", fieldsArray);
                modelArray.add(jsonModel);
            }
            jsonApp.add("models", modelArray);
        }

        //controllers
        JsonArrayBuilder controllerArray = Json.createArrayBuilder();
        if(mControllers.size() >0) {
            for (Map.Entry<String, Controller> entry : mControllers.entrySet()) {
                JsonObjectBuilder jsonControllers = Json.createObjectBuilder();
                Controller c = entry.getValue();
                jsonControllers.add("name",  c.getName());
                controllerArray.add(jsonControllers);
            }
        }
        jsonApp.add("controllers", controllerArray);

        JsonObjectBuilder jsonViews = Json.createObjectBuilder();
        if(mViews.size() >0) {
            for (Map.Entry<String, View> entry : mViews.entrySet()) {
                jsonViews.add("name", entry.getValue().getName());
            }
        }
        jsonApp.add("views",jsonViews);
//
        JsonObjectBuilder jsonFlows = Json.createObjectBuilder();
        if (mFlow.size() >0) {
            for (Flow entry : mFlow) {
                jsonFlows.add("flow", entry.toString());
            }
        }
        jsonApp.add("flows", jsonFlows);
//
//        JsonObjectBuilder jsonInterfaces = Json.createObjectBuilder();
//        if(mInterfaces.size() >0) {
//            for (Map.Entry<String, Interface> entry : mInterfaces.entrySet()) {
//                jsonInterfaces.add("name", entry.getValue().getName());
//            }
//        }
//        jsonApp.add("interfaces", jsonInterfaces);

        //array of spaces
        JsonArrayBuilder spaceArray = Json.createArrayBuilder();
        if(mSpace.size() >0) {
            for (Map.Entry<String, Space> entry : mSpace.entrySet()) {
                JsonObjectBuilder jsonSpaces = Json.createObjectBuilder();
                Space s = entry.getValue();
                //space name
                jsonSpaces.add("name", s.getName());

                //array of space controllers
                JsonArrayBuilder jsonSpaceCntrArray = Json.createArrayBuilder();
                //array of space processes
                JsonArrayBuilder jsonProcess = Json.createArrayBuilder();
                for(ConcreteController cc: s.getControllers()){
                    JsonObjectBuilder cntr = Json.createObjectBuilder();
                    cntr.add("name", cc.getName());
                    jsonSpaceCntrArray.add(cntr);
                    //populate array or processes
                    //For each of the controller and model we create a new process
                    for(ConcreteModel cm : cc.getLinkedModels()) {
                        JsonObjectBuilder process = Json.createObjectBuilder();
                        process.add("model", cm.getName());
                        process.add("controller", cc.getName());
                        jsonProcess.add(process);
                        // TODO:  get the actual event that controller listens to
                    }//end process
                } //end array of controllers
                jsonSpaces.add("controllers",jsonSpaceCntrArray);
                jsonSpaces.add("processes",jsonProcess);

                //array of space views

                JsonArrayBuilder spaceViewArray = Json.createArrayBuilder();
                //array of space displays
                JsonArrayBuilder spaceDisplayArray = Json.createArrayBuilder();
                for(ConcreteView cv: s.getViews()){
                    JsonObjectBuilder jsonSpaceViews = Json.createObjectBuilder();
                    jsonSpaceViews.add("name", cv.getName());
                    spaceViewArray.add(jsonSpaceViews);
                    //for each pair of a view and model create new display
                    for(Map.Entry<String, Model> mEntry: cv.getBaseView().getModels().entrySet()){
                        JsonObjectBuilder jsonDisplays = Json.createObjectBuilder();
                        jsonDisplays.add("view", cv.getName());
                        jsonDisplays.add("model", mEntry.getValue().getName());
                        spaceDisplayArray.add(jsonDisplays);
                    }//end loop for creating displays

                }//end loop for creating views
                jsonSpaces.add("views", spaceViewArray);
                jsonSpaces.add("displays", spaceDisplayArray);
                spaceArray.add(jsonSpaces);
            } // end of spaces
        }
        jsonApp.add("spaces", spaceArray);
        return jsonApp.build();
    }

    public String toString() {
        String ret ="Ravel applications: " + getmName() + "\n";
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
