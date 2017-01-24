package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.BinaryOperation;
import org.stanford.ravel.compiler.ir.UnaryOperation;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/20/17.
 */
public class TUnaryArithOp extends TInstruction {
    private final Type type;
    private final int target;
    private final int source;
    private final UnaryOperation op;

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
    int[] getSources() {
        return new int[]{ source };
    }

    @Override
    int getSink() {
        return target;
    }

    @Override
    Type[] getSourceTypes() {
        return new Type[]{ type };
    }

    @Override
    Type getSinkType() {
        return type;
    }
}