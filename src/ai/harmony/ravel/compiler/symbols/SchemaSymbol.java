package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.Scope;

/**
 * Created by lauril on 8/19/16.
 */
public class SchemaSymbol extends BlockSymbol {

    public SchemaSymbol(String name, Scope currentScope) throws SymbolNotAllowedInScopeException {
        super(name, currentScope);
        lexerCheck();
    }

    @Override
    public void lexerCheck() throws SymbolNotAllowedInScopeException {
//        if(! enclosingScope.getEnclosingScope().getT .equals("model")){
//            throw new SymbolNotAllowedInScopeException("Found schema in " + name + " allowed only in model");
//        }

    }

    @Override
    public String toString() {
        return "Schema Symbol: " + name;
    }
}
