package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;

import java.util.*;

/**
 * Created by lauril on 7/21/16.
 */
public class Controller extends Primitive implements Iterable<EventHandler> {
    private final List<EventHandler> mEvents = new ArrayList<>();
    private final Map<String, Type> mInterface =  new HashMap<>();
    private final List<VariableSymbol> mParamSymbols = new ArrayList<>();

    public Controller(String name) {
        super(name);
    }

    public void addEvent(EventHandler event) {
        mEvents.add(event);
    }

    public void addParameter(VariableSymbol sym) {
        mParamSymbols.add(sym);
        mInterface.put(sym.getName(), sym.getType());
    }
    public void addAllParameters(Collection<VariableSymbol> syms) {
        for (VariableSymbol sym : syms)
            addParameter(sym);
    }

    public Type getParameterType(String name) {
        return mInterface.get(name);
    }

    public boolean hasParameter(String pname) {
        return mInterface.containsKey(pname);
    }

    public Collection<String> getParameterNames() {
        return mInterface.keySet();
    }

    public List<VariableSymbol> getParameterSymbols() {
        return Collections.unmodifiableList(mParamSymbols);
    }

    public Iterator<EventHandler> iterator() {
        return mEvents.iterator();
    }

    public InstantiatedController instantiate(Space space, String varName) {
        return new InstantiatedController(space, this, varName);
    }
}
