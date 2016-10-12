package ai.harmony.ravel.primitives;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 7/21/16.
 */
public class Controller extends ParametrizedComponent{
    private static Logger LOGGER = Logger.getLogger(Controller.class.getName());

    private Map<String, Variable> mDeclarations = new LinkedHashMap<>();
    private Map<String, String> mReference = new LinkedHashMap<>();
    private Map<String, Event> mEvents = new LinkedHashMap<>();
    private Map<String, Model> mModels = new LinkedHashMap<>();
    private Map<String, Source> mSources = new LinkedHashMap<>();


    private Space mSpace;

    public Controller(String name){
        super(name, name+"Ctr");
    }
    //TODO: this is sparta
    public void setSpace(Space s){
        this.mSpace = s;
        for(String key: mModels.keySet()) {
            try {
                Model m = this.mSpace.resolveModel(key);
                mModels.put(key, m);
                m.addController(this);
            } catch (NullPointerException e){
                LOGGER.severe("Could not resolve model: " + key);
                e.printStackTrace();
            }

        }
        for(String key: mSources.keySet()) {
            //TODO: there is always a better way to handle this
            Source src = this.mSpace.getSource(mReference.get(key).replace("tier.sources.",""));
            if(src != null) {
                mSources.put(key, src);
                src.addController(this);
            }
        }

    }
    public void addVar(String name, Variable value) { this.mDeclarations.put(name, value) ;}

    public boolean isSubscribe(Model model){
        return true;
    }
    public void addRef(String name, String value) {
        if(value.contains("model")) {
            mModels.put(value, null);
        } else if (value.contains("source")){
            mSources.put(name, null);
        }
        this.mReference.put(name, value) ;}

    public List<Source> getSources(){
        List<Source> lst = new ArrayList<>();
        lst.addAll(mSources.values());
        return lst;

    }
    public List<Model> getModels(){
        List<Model> lst = new ArrayList<>();
        lst.addAll(mModels.values());
        return lst;

    }
    /**
     * To actually subscribe to source/model events we need to map
     * the events and get the actual source/model naming and make implementation
     *
     */

    public void addEvent(String name, Event event) {
        this.mEvents.put(name, event);
    }

    public String getfileNameC(){

        return mName+"_ctr";
    }


}
