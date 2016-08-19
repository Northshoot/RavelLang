package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.Scope;

/**
 * Created by lauril on 8/19/16.
 */
public class SourceSymbol extends BlockSymbol {
    public SourceSymbol(String name, Scope currentScope) throws SymbolNotAllowedInScopeException {
        super(name, currentScope);
    }

    @Override
    public void lexerCheck() throws SymbolNotAllowedInScopeException {

    }

    @Override
    public String toString() {
        return null;
    }
}
