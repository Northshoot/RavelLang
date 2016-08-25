package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.scopes.Scope;

/**
 * Created by lauril on 8/19/16.
 */
public class SpacePlatformSymbol extends BlockSymbol{

    public SpacePlatformSymbol(String name, Scope currentScope) {
        super(name, currentScope);

    }


    @Override
    public String toString() {
        return "Property Symbol:" + name;
    }
}
