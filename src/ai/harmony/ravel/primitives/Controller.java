package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;

import java.util.*;

/**
 * Created by lauril on 7/21/16.
 */
public class Controller extends Primitive{

    private Map<String, String> mDeclarations = new LinkedHashMap<>();
    private Map<String, Event> mEvents = new LinkedHashMap<>();

    public Controller(String name){
        super(name);
    }

    public void add(String name, String value) { this.mDeclarations.put(name, value) ;}
    public void add(String name, Event event) {
        this.mEvents.put(name, event);
        event.setController(this);
    }


}
