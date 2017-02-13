package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/20/17.
 */
public class TMove extends TInstruction {
    public final Type type;
    public int target;
    public int source;

    public TMove(Type type, int target, int source) {
        this.type = type;
        this.target = target;
        this.source = source;
    }

    public String toString() {
        return "move@" + type.getName() + " " + target + " = " + source;
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
}
