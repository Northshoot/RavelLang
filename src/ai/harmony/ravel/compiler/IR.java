package ai.harmony.ravel.compiler;

import ai.harmony.ravel.primitives.*;

import java.util.*;

/**
 * Class holding all
 * Created by lauril on 8/8/16.
 */
public class IR {
    Map<String, Primitive> mModelMap = new HashMap<>();
    Map<String, Primitive> mControllerMap = new HashMap<>();
    Map<String, Primitive> mSpaceMap = new HashMap<>();
    Map<String, Primitive> mFlowMap =new HashMap<>();
    private static IR singleton = null;
    public static IR getInstance() {
        if(singleton == null) {
            singleton = new IR();
        }
        return singleton;
    }

    public void add(String name, Primitive primitive) {
        if( primitive instanceof Model ) {
            this.mModelMap.put(name, primitive);
        } else if(primitive instanceof Controller){
            mControllerMap.put(name,primitive);
        } else if(primitive instanceof Space){
            mSpaceMap.put(name,primitive);
        } else if(primitive instanceof Flow){
            mFlowMap.put(name,primitive);
        }
    }

    public Model getModel(String name){
        return (Model) mModelMap.get(name);
    }

    public List<Model> getModels(){
        List<Model> modelList = new LinkedList<>();
        for( Primitive m : mModelMap.values() ){
            modelList.add((Model)m);
        }
        return modelList;
    }

    public List<Controller> getControllers(){
        List<Controller> modelList = new LinkedList<>();
        for( Primitive m : mControllerMap.values() ){
            modelList.add((Controller)m);
        }
        return modelList;
    }

    public List<Space> getSpaces(){
        List<Space> modelList = new LinkedList<>();
        for( Primitive m : mSpaceMap.values() ){
            modelList.add((Space) m);
        }
        return modelList;
    }

    public List<Flow> getFlows(){
        List<Flow> modelList = new LinkedList<>();
        for( Primitive m : mFlowMap.values() ){
            modelList.add((Flow) m);
        }
        return modelList;
    }
    public Controller getController(String name){
        return (Controller) mControllerMap.get(name);
    }
    public Space getSpace(String name){
        return (Space) mSpaceMap.get(name);
    }
    public Flow getFlow(String name){
        return (Flow) mFlowMap.get(name);
    }

    public  IR(){

    }
}
