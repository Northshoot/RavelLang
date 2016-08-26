package ai.harmony.ravel.compiler.symbol;

import ai.harmony.ravel.antlr4.RavelParser;

/**
 * Created by lauril on 8/25/16.
 */
public class FieldSymbol extends BaseSymbol implements TypedSymbol {
    public FieldSymbol(String name) {
        super(name);

    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }
}
