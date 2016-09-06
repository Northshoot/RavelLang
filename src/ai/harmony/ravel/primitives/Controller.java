package ai.harmony.ravel.primitives;

import java.util.*;

/**
 * Created by lauril on 7/21/16.
 */
public class Controller extends Primitive{

    private Map<String, Variable> mDeclarations = new LinkedHashMap<>();
    private Map<String, String> mReference = new LinkedHashMap<>();
    private Map<String, Event> mEvents = new LinkedHashMap<>();
    private List<String> mParams = new ArrayList<>();

    public Controller(String name){
        super(name);
    }

    public void addVar(String name, Variable value) { this.mDeclarations.put(name, value) ;}

    public void addRef(String name, String value) { this.mReference.put(name, value) ;}

    public void addEvent(String name, Event event) {
        this.mEvents.put(name, event);
    }

    public void addParam(String p){
        mParams.add(p);
    }

    public boolean hasParam(String p){
        for(String str: mParams) {
            if(str.trim().contains(p))
                return true;
        }
        return false;
    }

    public List<String> getParams() { return  this.mParams; }
}
