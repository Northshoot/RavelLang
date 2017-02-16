package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.ComparisonOperation;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

import java.util.Objects;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TComparisonOp extends TInstruction {
    public final Type type;
    public int target;
    public int src1;
    public int src2;
    public final ComparisonOperation op;

    public TComparisonOp(Type type, int target, int src1, int src2, ComparisonOperation op) {
        assert op != null;
        assert op.isLegalType(type);

        this.type = type;
        this.target = target;
        this.src1 = src1;
        this.src2 = src2;
        this.op = op;
    }

    public String toString() {
        return "comp@" + type.getName() + " " + target + " = " + src1 + " " + op + " " + src2;
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
        return PrimitiveType.BOOL;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluate(Object[] args) {
        switch (op) {
            case EQUAL:
                return Objects.equals(args[0], args[1]);
            case NOTEQUAL:
                return !Objects.equals(args[0], args[1]);
            case GT:
                if (type == PrimitiveType.BYTE)
                    return (byte)args[0] > (byte)args[1];
                else if (type == PrimitiveType.INT32)
                    return (int)args[0] > (int)args[1];
                else if (type == PrimitiveType.DOUBLE)
                    return (double)args[0] > (double)args[1];
                else if (type == PrimitiveType.STR)
                    return ((String)args[0]).compareTo((String)args[1]) > 0;
                else
                    throw new AssertionError();
            case LT:
                if (type == PrimitiveType.BYTE)
                    return (byte)args[0] < (byte)args[1];
                else if (type == PrimitiveType.INT32)
                    return (int)args[0] < (int)args[1];
                else if (type == PrimitiveType.DOUBLE)
                    return (double)args[0] < (double)args[1];
                else if (type == PrimitiveType.STR)
                    return ((String)args[0]).compareTo((String)args[1]) < 0;
                else
                    throw new AssertionError();
            case LE:
                if (type == PrimitiveType.BYTE)
                    return (byte)args[0] <= (byte)args[1];
                else if (type == PrimitiveType.INT32)
                    return (int)args[0] <= (int)args[1];
                else if (type == PrimitiveType.DOUBLE)
                    return (double)args[0] <= (double)args[1];
                else if (type == PrimitiveType.STR)
                    return ((String)args[0]).compareTo((String)args[1]) <= 0;
                else
                    throw new AssertionError();
            case GE:
                if (type == PrimitiveType.BYTE)
                    return (byte)args[0] >= (byte)args[1];
                else if (type == PrimitiveType.INT32)
                    return (int)args[0] >= (int)args[1];
                else if (type == PrimitiveType.DOUBLE)
                    return (double)args[0] >= (double)args[1];
                else if (type == PrimitiveType.STR)
                    return ((String)args[0]).compareTo((String)args[1]) >= 0;
                else
                    throw new AssertionError();
            default:
                throw new AssertionError();
        }
    }

}
