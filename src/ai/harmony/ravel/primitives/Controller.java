package ai.harmony.ravel.primitives;

import java.util.*;

/**
 * Created by lauril on 7/21/16.
 */
public class Controller extends ParametrizedComponent{


    private Map<String, Variable> mDeclarations = new LinkedHashMap<>();
    private Map<String, String> mReference = new LinkedHashMap<>();
    private Map<String, Event> mEvents = new LinkedHashMap<>();
    private Map<String, Model> mModels = new LinkedHashMap<>();

    private Space mSpace;

    public Controller(String name){
        super(name);
    }
    //TODO: this is sparta
    public void setSpace(Space s){
        this.mSpace = s;
        for(String key: mModels.keySet()) {
            Model m = this.mSpace.resolveModel(key);
            mModels.put(key, m);
            m.addController(this);
        }
    }
    public void addVar(String name, Variable value) { this.mDeclarations.put(name, value) ;}

    public void addRef(String name, String value) {
        if(value.contains("model"))
            mModels.put(value, null);
        this.mReference.put(name, value) ;}



    public void addEvent(String name, Event event) {
        this.mEvents.put(name, event);
    }

    public String getName_c(){ return "m_"+mName.toLowerCase()+"_ctr";}

    public void addModels(Map<String, Model> modelMap) {

    }
}
