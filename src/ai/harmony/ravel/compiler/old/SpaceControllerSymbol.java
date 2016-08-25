package ai.harmony.ravel.compiler.old;

import ai.harmony.ravel.error.exceptions.SymbolNotAllowedInScopeException;

/**
 * Created by lauril on 8/19/16.
 */
public class SpaceControllerSymbol extends BlockSymbol {
    public SpaceControllerSymbol(String name, Scope currentScope) throws SymbolNotAllowedInScopeException {
        super(name, currentScope);
    }


    @Override
    public String toString() {
        return "SpaceController Symbol: " + name;
    }
}
