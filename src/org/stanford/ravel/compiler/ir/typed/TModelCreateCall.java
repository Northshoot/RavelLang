package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TModelCreateCall extends TInstruction {
    public final int target;
    public final ModelSymbol model;
    public final Type type;

    public TModelCreateCall(Type type, int target, ModelSymbol value) {
        this.type = type;
        this.target = target;
        this.model = value;
    }

    public String toString() {
        return "model.create@" + type.getName() + " " + target + " = '" + model.getName() + "'";
    }

    @Override
    int getSink() {
        return target;
    }

    @Override
    Type getSinkType() {
        return type;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
