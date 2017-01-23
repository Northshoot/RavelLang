package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/20/17.
 */
public class TImmediateLoad extends TInstruction {
    private final int target;
    private final Object value;
    private final Type type;

    public TImmediateLoad(Type type, int target, Object value) {
        this.type = type;
        this.target = target;
        this.value = value;
    }

    public String toString() {
        return "loadimm@" + type.getName() + " " + target + " = '" + value + "'";
    }

    @Override
    int getSink() {
        return target;
    }

    @Override
    Type getSinkType() {
        return type;
    }
}
