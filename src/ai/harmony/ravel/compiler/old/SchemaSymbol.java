package ai.harmony.ravel.compiler.old;

import ai.harmony.ravel.error.exceptions.SymbolNotAllowedInScopeException;

/**
 * Created by lauril on 8/19/16.
 */
public class SchemaSymbol extends BlockSymbol {

    public SchemaSymbol(String name, Scope currentScope) throws SymbolNotAllowedInScopeException {
        super(name, currentScope);

    }


    @Override
    public String toString() {
        return "Schema Symbol: " + name;
    }
}
