package ai.harmony.ravel.primitives;

import java.util.*;

/**
 * Created by lauril on 7/21/16.
 */
public class Controller extends ParametrizedComponent{


    private Map<String, Variable> mDeclarations = new LinkedHashMap<>();
    private Map<String, String> mReference = new LinkedHashMap<>();
    private Map<String, Event> mEvents = new LinkedHashMap<>();


    public Controller(String name){
        super(name);
    }

    public void addVar(String name, Variable value) { this.mDeclarations.put(name, value) ;}

    public void addRef(String name, String value) { this.mReference.put(name, value) ;}

    public void addEvent(String name, Event event) {
        this.mEvents.put(name, event);
    }



}
