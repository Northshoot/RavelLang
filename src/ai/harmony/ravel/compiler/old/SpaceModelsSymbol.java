package ai.harmony.ravel.compiler.old;

import ai.harmony.ravel.error.exceptions.SymbolNotAllowedInScopeException;

/**
 * Created by lauril on 8/19/16.
 */
public class SpaceModelsSymbol extends BlockSymbol{
    public SpaceModelsSymbol(String name, Scope currentScope) throws SymbolNotAllowedInScopeException {
        super(name, currentScope);
    }


    @Override
    public String toString() {
        return "SpaceModels Symbol: " + name;
    }
}
