package org.stanford.ravel.primitives;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 7/21/16.
 */
public class Controller extends Primitive {
    private static Logger LOGGER = Logger.getLogger(Controller.class.getName());

    private final List<Event> mEvents = new ArrayList<>();
    private final Map<String, Model> mModels = new LinkedHashMap<>();
    private final Map<String, Source> mSources = new LinkedHashMap<>();


    private Space mSpace;

    public Controller(String name){
        super(name, name+"Ctr");
    }

    public List<Source> getSources(){
        List<Source> lst = new ArrayList<>();
        lst.addAll(mSources.values());
        return lst;

    }
    public List<Model> getModels() {
        List<Model> lst = new ArrayList<>();
        lst.addAll(mModels.values());
        return lst;
    }
}
