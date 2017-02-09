package org.stanford.ravel.compiler.ir.typed;

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
    int[] getSources() {
        return new int[]{ source };
    }

    @Override
    public int getSink() {
        return target;
    }

    @Override
    Type[] getSourceTypes() {
        return new Type[]{ srcType };
    }

    @Override
    public Type getSinkType() {
        return tgtType;
    }

    @Override
    boolean writesMemory() {
        return false;
    }

    @Override
    boolean readsMemory() {
        return false;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
