package ai.harmony.ravel.compiler.scope;

import ai.harmony.ravel.compiler.symbol.*;
import ai.harmony.ravel.compiler.types.IntrinsicTypes;
import ai.harmony.ravel.compiler.types.PrimitiveType;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/25/16.
 */
public class GlobalScope extends BaseScope {
    private Map<String, ModelSymbol> models = new LinkedHashMap<>();
    private Map<String, ControllerSymbol> controllers = new LinkedHashMap<>();
    private Map<String, SpaceSymbol> spaces = new LinkedHashMap<>();
    private Map<String, InterfaceSymbol> interfaces = new LinkedHashMap<>();
    private Map<String, ViewSymbol> views = new LinkedHashMap<>();

    public GlobalScope() {
        super();

        // define primitive types in scope
        for (PrimitiveType type : PrimitiveType.values()) {
            if (type.isValid())
                define(new PrimitiveTypeSymbol(type));
        }
        define(new PrimitiveTypeSymbol(IntrinsicTypes.ENDPOINT));
    }
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
        else if (sym instanceof InterfaceSymbol)
            interfaces.put(sym.getName(), (InterfaceSymbol) sym);
        else if (sym instanceof ViewSymbol)
            views.put(sym.getName(), (ViewSymbol)sym);
        else if (!(sym instanceof PrimitiveTypeSymbol))
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

    public Collection<InterfaceSymbol> getInterfaces() {
        return interfaces.values();
    }

    public Collection<ViewSymbol> getViews() {
        return views.values();
    }
}
