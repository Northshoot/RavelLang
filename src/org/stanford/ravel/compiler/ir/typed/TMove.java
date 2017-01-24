package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/20/17.
 */
public class TMove extends TInstruction {
    private final Type type;
    private final int target;
    private final int source;

    public TMove(Type type, int target, int source) {
        this.type = type;
        this.target = target;
        this.source = source;
    }

    public String toString() {
        return "move@" + type.getName() + " " + target + " = " + source;
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