package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.types.Type;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 7/21/16.
 */
public class Controller extends Primitive implements Iterable<Event> {
    private final List<Event> mEvents = new ArrayList<>();
    private final Map<String, Type> mInterface =  new HashMap<>();

    public Controller(String name) {
        super(name, name+"Ctr");
    }

    public void addEvent(Event event) {
        mEvents.add(event);
    }

    public void addParameter(String name, Type type) {
        mInterface.put(name, type);
    }

    public Type getParameterType(String name) {
        return mInterface.get(name);
    }

    public Collection<String> getParameterNames() {
        return mInterface.keySet();
    }

    public Iterator<Event> iterator() {
        return mEvents.iterator();
    }

    public InstantiatedController instantiate(Space space) {
        return new InstantiatedController(space, this);
    }
}
