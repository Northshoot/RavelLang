package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.BinaryOperation;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/20/17.
 */
public class TBinaryArithOp extends TInstruction {
    private final Type type;
    private final int target;
    private final int src1;
    private final int src2;
    private final BinaryOperation op;

    public TBinaryArithOp(Type type, int target, int src1, int src2, BinaryOperation op) {
        this.type = type;
        this.target = target;
        this.src1 = src1;
        this.src2 = src2;
        this.op = op;
    }

    public String toString() {
        return "binarith@" + type.getName() + " " + target + " = " + src1 + " " + op + " " + src2;
    }

    @Override
    int[] getSources() {
        return new int[]{ src1, src2 };
    }

    @Override
    int getSink() {
        return target;
    }

    @Override
    Type[] getSourceTypes() {
        return new Type[]{ type, type };
    }

    @Override
    Type getSinkType() {
        return type;
    }
}
