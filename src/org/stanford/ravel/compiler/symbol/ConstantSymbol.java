package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.ParserUtils;

/**
 * A symbol representing a constant, defined in a space
 * or a model
 *
 * Created by gcampagn on 1/26/17.
 */
public class ConstantSymbol extends BaseSymbol {
    private final Object value;

    public ConstantSymbol(String name, Object value) {
        super(name);
        this.value = value;
        setType(ParserUtils.typeFromLiteral(value));
    }

    public Object getValue() {
        return value;
    }
}
