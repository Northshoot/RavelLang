package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.Scope;

/**
 * Created by lauril on 8/19/16.
 */
public class ModelPropertySymbol extends BlockSymbol{

    public ModelPropertySymbol(String name, Scope currentScope){
        super(name, currentScope);
    }



    @Override
    public String toString() {
        return "Property Symbol:" + name;
    }
}
