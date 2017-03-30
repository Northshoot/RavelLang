package edu.stanford.ravel.compiler.ir.typed;

import edu.stanford.ravel.compiler.ir.BinaryOperation;
import edu.stanford.ravel.compiler.types.PrimitiveType;
import edu.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/20/17.
 */
public class TBinaryArithOp extends TInstruction {
    public final Type type;
    public int target;
    public int src1;
    public int src2;
    public final BinaryOperation op;

    public TBinaryArithOp(Type type, int target, int src1, int src2, BinaryOperation op) {
        assert op.isLegalType(type);
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
    public int[] getSources() {
        return new int[]{ src1, src2 };
    }

    @Override
    public int getSink() {
        return target;
    }

    @Override
    public Type[] getSourceTypes() {
        return new Type[]{ type, type };
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
            case POW:
                assert type == PrimitiveType.DOUBLE;
                return Math.pow((double) args[0], (double) args[1]);
            case DIV:
                assert type == PrimitiveType.DOUBLE;
                return (double) args[0] / (double) args[1];
            case MUL:
                if (type == PrimitiveType.DOUBLE)
                    return (double) args[0] * (double) args[1];
                else if (type == PrimitiveType.INT32)
                    return (int) args[0] * (int) args[1];
                else
                    throw new AssertionError();
            case IDIV:
                if (type == PrimitiveType.DOUBLE)
                    return (int) Math.floor((double) args[0] / (double) args[1]);
                else if (type == PrimitiveType.INT32)
                    return (int) args[0] / (int) args[1];
                else
                    throw new AssertionError();
            case MOD:
                if (type == PrimitiveType.DOUBLE)
                    return (double) args[0] % (double) args[1];
                else if (type == PrimitiveType.INT32)
                    return (int) args[0] % (int) args[1];
                else
                    throw new AssertionError();
            case SUB:
                if (type == PrimitiveType.DOUBLE)
                    return (double) args[0] - (double) args[1];
                else if (type == PrimitiveType.INT32)
                    return (int) args[0] - (int) args[1];
                else
                    throw new AssertionError();
            case ADD:
                if (type == PrimitiveType.DOUBLE)
                    return (double) args[0] + (double) args[1];
                else if (type == PrimitiveType.INT32)
                    return (int) args[0] + (int) args[1];
                else if (type == PrimitiveType.STR)
                    return (String) args[0] + (String) args[1];
                else
                    throw new AssertionError();
            case LSHIFT:
                if (type == PrimitiveType.INT32)
                    return (int) args[0] << (int) args[1];
                else if (type == PrimitiveType.BYTE)
                    return (byte) ((byte) args[0] << (byte) args[1]);
                else
                    throw new AssertionError();
            case RSHIFT:
                if (type == PrimitiveType.INT32)
                    return (int) args[0] >> (int) args[1];
                else if (type == PrimitiveType.BYTE)
                    return (byte) ((byte) args[0] >> (byte) args[1]);
                else
                    throw new AssertionError();
            case AND:
                if (type == PrimitiveType.INT32)
                    return (int) args[0] & (int) args[1];
                else if (type == PrimitiveType.BYTE)
                    return (byte) ((byte) args[0] & (byte) args[1]);
                else
                    throw new AssertionError();
            case OR:
                if (type == PrimitiveType.INT32)
                    return (int) args[0] | (int) args[1];
                else if (type == PrimitiveType.BYTE)
                    return (byte) ((byte) args[0] | (byte) args[1]);
                else
                    throw new AssertionError();
            case XOR:
                if (type == PrimitiveType.INT32)
                    return (int) args[0] ^ (int) args[1];
                else if (type == PrimitiveType.BYTE)
                    return (byte) ((byte) args[0] ^ (byte) args[1]);
                else
                    throw new AssertionError();
            default:
                throw new AssertionError();
        }
    }

}
