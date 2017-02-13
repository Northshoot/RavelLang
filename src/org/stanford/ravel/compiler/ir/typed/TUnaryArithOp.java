package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.UnaryOperation;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/20/17.
 */
public class TUnaryArithOp extends TInstruction {
    public final Type type;
    public int target;
    public int source;
    public final UnaryOperation op;

    public TUnaryArithOp(Type type, int target, int source, UnaryOperation op) {
        this.type = type;
        this.target = target;
        this.source = source;
        this.op = op;
    }

    public String toString() {
        return "unarith@" + type.getName() + " " + target + " = " + op + " " + source;
    }

    @Override
    public int[] getSources() {
        return new int[]{ source };
    }

    @Override
    public int getSink() {
        return target;
    }

    @Override
    public Type[] getSourceTypes() {
        return new Type[]{ type };
    }

    @Override
    public Type getSinkType() {
        return type;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
