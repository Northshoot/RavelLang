package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.UnaryOperation;
import org.stanford.ravel.compiler.types.PrimitiveType;
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
        assert op.isLegalType(type);
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

    @Override
    public Object evaluate(Object[] args) {
        switch (op) {
            case NEG:
                if (type == PrimitiveType.DOUBLE)
                    return -(double)args[0];
                else if (type == PrimitiveType.INT32)
                    return -(int)args[0];
                else
                    throw new AssertionError();
            case PLUS:
                if (type == PrimitiveType.DOUBLE)
                    return +(double)args[0];
                else if (type == PrimitiveType.INT32)
                    return +(int)args[0];
                else
                    throw new AssertionError();
            case BNOT:
                if (type == PrimitiveType.INT32)
                    return ~(int)args[0];
                else if (type == PrimitiveType.BYTE)
                    return (byte)~(byte)args[0];
                else
                    throw new AssertionError();
            case NOT:
                assert type == PrimitiveType.BOOL;
                return !(boolean)args[0];
            default:
                throw new AssertionError();
        }
    }

}
