package edu.stanford.ravel.compiler.ir.typed;

import edu.stanford.ravel.compiler.ParserUtils;
import edu.stanford.ravel.compiler.types.PrimitiveType;
import edu.stanford.ravel.compiler.types.Type;

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

        return ParserUtils.convertLiterals(tgtType, srcType, args[0]);
    }

}
