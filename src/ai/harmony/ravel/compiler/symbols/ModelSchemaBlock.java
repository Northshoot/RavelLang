package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.scopes.Scope;
import ai.harmony.ravel.primitives.Field;

/**
 * Created by lauril on 8/19/16.
 */
public class ModelSchemaBlock extends BlockSymbol{

    public ModelSchemaBlock(String name, Scope currentScope) {
        super(name, currentScope);

    }





    @Override
    public String toString() {
        return "Schema Symbol:" + name;
    }
}
