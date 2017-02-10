package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.FieldSymbol;
import org.stanford.ravel.compiler.types.Type;

/**
 * A model field, which has a name, a type, and a set of security/operation tags
 * computed by the various analysis passes
 *
 * Created by gcampagn on 2/9/17.
 */
public class ModelField {
    private final FieldSymbol symbol;

    public ModelField(FieldSymbol symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return symbol.getName();
    }

    public Type getType() {
        return symbol.getType();
    }

    public String toString() {
        return "Field: " + getName() + " @" + getType().getName();
    }
}
