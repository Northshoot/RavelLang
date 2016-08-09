package ai.harmony.ravel.compiler;

import ai.harmony.ravel.primitives.Controller;
import ai.harmony.ravel.primitives.Model;
import ai.harmony.ravel.primitives.Space;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class holding all
 * Created by lauril on 8/8/16.
 */
public class IR {
    Map<String, Model> modelMap = new LinkedHashMap<>();
    Map<String, Controller> controllerMap = new LinkedHashMap<>();
    Map<String, Space> spaceMap = new LinkedHashMap<>();

    public IR() {

    }

    public void addModel(String name, Model model){
        modelMap.put(name, model);
    }

    public void addController(String name, Controller c){
        controllerMap.put(name, c);
    }

    public void addSpace(String name, Space s){
        spaceMap.put(name, s);
    }

    public Map<String, Model> getModelMap() {
        return modelMap;
    }

    public void setModelMap(Map<String, Model> modelMap) {
        this.modelMap = modelMap;
    }

    public Map<String, Controller> getControllerMap() {
        return controllerMap;
    }

    public void setControllerMap(Map<String, Controller> controllerMap) {
        this.controllerMap = controllerMap;
    }

    public Map<String, Space> getSpaceMap() {
        return spaceMap;
    }

    public void setSpaceMap(Map<String, Space> spaceMap) {
        this.spaceMap = spaceMap;
    }
}
