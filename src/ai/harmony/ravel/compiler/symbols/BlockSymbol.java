package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.Scope;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/19/16.
 */
public abstract class BlockSymbol extends Symbol implements Scope {
    Map<String, Symbol> assigments = new LinkedHashMap<>();
    Scope enclosingScope;

    //models will take arguments
    Map<String, Symbol> orderedArgs = new LinkedHashMap<>();

    public BlockSymbol(String name, Scope currentScope) throws SymbolNotAllowedInScopeException {
        super(name, Type.BLOCK);
        this.enclosingScope = currentScope;


    }


    public Symbol resolve(String name) {
        Symbol s = assigments.get(name);
        if ( s!=null ) return s;
        // if not here, check any enclosing scope
        if ( getEnclosingScope() != null ) {
            return getEnclosingScope().resolve(name);
        }
        return null; // not found
    }

    public void define(Symbol sym) {
        assigments.put(sym.name, sym);
        sym.scope = this; // track the scope in each symbol
    }

    public Scope getEnclosingScope() { return enclosingScope; }
    public String getScopeName() { return name; }

    public abstract void lexerCheck() throws SymbolNotAllowedInScopeException;
    public abstract String toString();

}
