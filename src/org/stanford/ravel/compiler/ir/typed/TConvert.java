package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TConvert extends TInstruction {
    public final Type srcType;
    public final Type tgtType;
    public int target;
    public int source;

    public TConvert(Type tgtType, Type srcType, int target, int source) {
        if (tgtType.equals(srcType))
            throw new IllegalArgumentException("Invalid conversion");
        this.tgtType = tgtType;
        this.srcType = srcType;
        this.target = target;
        this.source = source;
    }

    public String toString() {
        return "convert " + target + " = (" + srcType.getName() + " -> " + tgtType.getName() + ") " + source;
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
        return new Type[]{ srcType };
    }

    @Override
    public Type getSinkType() {
        return tgtType;
    }

    @Override
    public boolean writesMemory() {
        return false;
    }

    @Override
    public boolean readsMemory() {
        return false;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }

    // NOTE: TConvert for now is only used for implicit conversions
    // because there are no cast operators in the language
    // If a cast operator is included then this needs to be extended
    @Override
    public Object evaluate(Object[] args) {
        if (tgtType.equalsExceptQualifiers(srcType))
            return args[0];
        if (!(tgtType instanceof PrimitiveType))
            return null;

        switch ((PrimitiveType)tgtType) {
            case ANY:
                return null;

            case BOOL:
                if (srcType == PrimitiveType.ERROR_MSG)
                    return null;
                assert srcType == PrimitiveType.BOOL;
                return (boolean)args[0];
            case BYTE:
                if (srcType == PrimitiveType.BOOL)
                    return (byte)(((boolean)args[0]) ? 1 : 0);
                else if (srcType == PrimitiveType.BYTE)
                    return (byte)args[0];
                else if (srcType == PrimitiveType.INT32)
                    return (byte)(int)args[0];
                else if (srcType == PrimitiveType.DOUBLE)
                    return (byte)(double)args[0];
                else
                    throw new AssertionError();
            case INT32:
                if (srcType == PrimitiveType.BOOL)
                    return ((boolean)args[0]) ? 1 : 0;
                else if (srcType == PrimitiveType.BYTE)
                    return (int)(byte)args[0];
                else if (srcType == PrimitiveType.INT32)
                    return (int)args[0];
                else if (srcType == PrimitiveType.DOUBLE)
                    return (int)(double)args[0];
                else
                    throw new AssertionError();
            case DOUBLE:
                if (srcType == PrimitiveType.BOOL)
                    return ((boolean)args[0]) ? 1.0 : 0.0;
                else if (srcType == PrimitiveType.INT32)
                    return (double)(int)args[0];
                else if (srcType == PrimitiveType.DOUBLE)
                    return (double)args[0];
                else
                    throw new AssertionError();
        }
        return null;
    }

}
