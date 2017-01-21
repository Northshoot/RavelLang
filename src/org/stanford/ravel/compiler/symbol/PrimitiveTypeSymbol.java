package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

/**
 * A symbol corresponding to a primitive type
 *
 * Symbols of this class are created by GlobalScope and always available
 *
 * Created by gcampagn on 1/20/17.
 */
public class PrimitiveTypeSymbol extends BaseSymbol implements TypeSymbol {
    private final PrimitiveType type;

    public PrimitiveTypeSymbol(PrimitiveType type) {
        super(type.getName());
        this.type = type;
    }

    public PrimitiveType getDefinedType() {
        return type;
    }
}
