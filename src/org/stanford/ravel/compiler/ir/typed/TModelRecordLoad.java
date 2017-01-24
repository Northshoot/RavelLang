package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.types.Type;

/**
 * Load a pointer to the current record of a model into a register
 *
 * Created by gcampagn on 1/23/17.
 */
public class TModelRecordLoad extends TInstruction {
    private final int target;
    private final ModelSymbol model;
    private final Type type;

    public TModelRecordLoad(Type type, int target, ModelSymbol value) {
        this.type = type;
        this.target = target;
        this.model = value;
    }

    public String toString() {
        return "model.record@" + type.getName() + " " + target + " = '" + model.getName() + "'";
    }

    @Override
    int getSink() {
        return target;
    }

    @Override
    Type getSinkType() {
        return type;
    }
}
