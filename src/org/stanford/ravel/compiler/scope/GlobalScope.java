package org.stanford.ravel.compiler.scope;

import org.stanford.ravel.compiler.symbol.ControllerSymbol;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.symbol.SpaceSymbol;
import org.stanford.ravel.compiler.symbol.Symbol;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/25/16.
 */
public class GlobalScope extends BaseScope {
    private Map<String, ModelSymbol> models = new LinkedHashMap<>();
    private Map<String, ControllerSymbol> controllers = new LinkedHashMap<>();
    private Map<String, SpaceSymbol> spaces = new LinkedHashMap<>();

    public GlobalScope() { super(); }
    public String getName() { return "global"; }

    public String toString() { return "Global Symbol"; }

    @Override
    public void setEnclosingScope(Scope enclosingScope) {
        throw new AssertionError("Global scope cannot be enclosed");
    }

    public void define(Symbol sym) throws IllegalArgumentException {
        super.define(sym);

        if (sym instanceof ModelSymbol)
            models.put(sym.getName(), (ModelSymbol)sym);
        else if (sym instanceof ControllerSymbol)
            controllers.put(sym.getName(), (ControllerSymbol)sym);
        else if (sym instanceof SpaceSymbol)
            spaces.put(sym.getName(), (SpaceSymbol)sym);
        else
            throw new IllegalArgumentException("Invalid toplevel symbol " + sym.getName());
    }

    public Collection<ModelSymbol> getModels() {
        return models.values();
    }

    public Collection<ControllerSymbol> getControllers() {
        return controllers.values();
    }

    public Collection<SpaceSymbol> getSpaces() {
        return spaces.values();
    }
}
