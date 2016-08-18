package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.scopes.Scope;
import ai.harmony.ravel.compiler.scopes.ScopedSymbol;
import ai.harmony.ravel.primitives.ModelType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/18/16.
 */
public class ModelSymbol extends ScopedSymbol {
    String mName;
    ModelType mModelType;

    Map<String, Symbol> orderedArgs = new LinkedHashMap<String, Symbol>();

    public ModelSymbol(String name, ModelType mType, Scope currentScope) {
        super(name, "model", currentScope);
    }

    public Map<String, Symbol> getMembers() { return orderedArgs; }

    public String getName() {
        return name+"("+stripBrackets(orderedArgs.keySet().toString())+")";
    }
}
