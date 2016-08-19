package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.Scope;

/**
 * Created by lauril on 8/19/16.
 */
public class PropertySymbol extends BlockSymbol{

    public PropertySymbol(String name, Scope currentScope) throws SymbolNotAllowedInScopeException {
        super(name, currentScope);
        lexerCheck();
    }

    @Override
    public void lexerCheck() throws SymbolNotAllowedInScopeException {
        System.out.println("Enclosing scope: " + super.getEnclosingScope().getScopeName() );
    }

    @Override
    public String toString() {
        return null;
    }
}
