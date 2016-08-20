package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.Scope;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/19/16.
 */
public abstract class BlockSymbol extends Symbol implements Scope {
    Map<String, Symbol> mVars = new LinkedHashMap<>();
    Scope enclosingScope;


    public BlockSymbol(String name, Scope currentScope) throws SymbolNotAllowedInScopeException {
        super(name, Type.BLOCK);
        this.enclosingScope = currentScope;


    }


    public Symbol resolve(String name) {
        Symbol s = mVars.get(name);
        if ( s!=null ) return s;
        // if not here, check any enclosing scope
        if ( getEnclosingScope() != null ) {
            return getEnclosingScope().resolve(name);
        }
        return null; // not found
    }

    public void define(Symbol sym) {
        if(sym == null){
            System.err.println("can't define null symbol!");
            System.exit(-1);
        }
        mVars.put(sym.name, sym);
        sym.scope = this; // track the scope in each symbol
    }


    public Scope getEnclosingScope() { return enclosingScope; }
    public String getScopeName() { return name; }

    public abstract void lexerCheck() throws SymbolNotAllowedInScopeException;
    public abstract String toString();

}
