package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.Type;

/**
 * Created by lauril on 8/25/16.
 */
public class FieldSymbol extends BaseSymbol implements TypedSymbol {
    public FieldSymbol(String name) {
        super(name);
    }
}
