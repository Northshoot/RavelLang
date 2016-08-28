package ai.harmony.ravel.primitives;

import ai.harmony.ravel.compiler.symbol.BaseSymbol;
import ai.harmony.ravel.compiler.symbol.Type;
import ai.harmony.ravel.compiler.symbol.TypedSymbol;

/**
 * Created by lauril on 8/26/16.
 */
public class InstanceSymbol extends BaseSymbol implements TypedSymbol {

    public InstanceSymbol(String name) {
        super(name);
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }
}