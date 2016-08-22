package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.Scope;

/**
 * Created by lauril on 8/19/16.
 */
public class ConfigurationSymbol extends BlockSymbol {

    public ConfigurationSymbol(String name, Scope currentScope) throws SymbolNotAllowedInScopeException {
        super(name, currentScope);

    }


    @Override
    public String toString() {
        return "Configuration Symbol: " +name;
    }
}
