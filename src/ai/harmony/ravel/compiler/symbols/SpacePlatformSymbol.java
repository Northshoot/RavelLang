package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.scopes.Scope;

/**
 * Created by lauril on 8/19/16.
 */
public class SpacePlatformSymbol extends BlockSymbol{

    public SpacePlatformSymbol(String name, Type mType, Scope currentScope) {
        super(name,mType, currentScope);

    }


    @Override
    public String toString() {
        return "Property Symbol:" + name;
    }
}
