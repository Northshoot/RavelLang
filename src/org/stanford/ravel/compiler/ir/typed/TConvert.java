package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TConvert extends TInstruction {
    private final Type srcType;
    private final Type tgtType;
    private final int target;
    private final int source;

    public TConvert(Type tgtType, Type srcType, int target, int source) {
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
    int getSink() {
        return target;
    }

    @Override
    Type[] getSourceTypes() {
        return new Type[]{ srcType };
    }

    @Override
    Type getSinkType() {
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
}
