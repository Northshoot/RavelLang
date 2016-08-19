package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.Scope;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/19/16.
 */
public class ControllerSymbol extends Symbol implements Scope {
    Map<String, Symbol> arguments = new LinkedHashMap<>();
    Scope enclosingScope;

    //models will take arguments
    Map<String, Symbol> orderedArgs = new LinkedHashMap<>();
    public ControllerSymbol(String name, Symbol.Type mType, Scope currentScope) {
        super(name, mType);
        this.enclosingScope = currentScope;
    }



    public Symbol resolve(String name) {
        Symbol s = arguments.get(name);
        if ( s!=null ) return s;
        // if not here, check any enclosing scope
        if ( getEnclosingScope() != null ) {
            return getEnclosingScope().resolve(name);
        }
        return null; // not found
    }

    public void define(Symbol sym) {
        arguments.put(sym.name, sym);
        sym.scope = this; // track the scope in each symbol
    }

    public Scope getEnclosingScope() { return enclosingScope; }
    public String getScopeName() { return name; }

    public String toString() { return "Controller"+super.toString()+":"+arguments.values(); }
}
