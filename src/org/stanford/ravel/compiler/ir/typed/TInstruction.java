package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

import static org.stanford.ravel.compiler.ir.Registers.VOID_REG;

/**
 * Created by gcampagn on 1/20/17.
 */
public abstract class TInstruction {
    public int[] getSources() {
        return new int[0];
    }
    public Type[] getSourceTypes() {
        return new Type[0];
    }

    public int getSink() {
        return VOID_REG;
    }

    public Type getSinkType() {
        return PrimitiveType.VOID;
    }

    public boolean writesMemory() {
        return false;
    }

    public boolean readsMemory() {
        return false;
    }

    public boolean affectsControlFlow() {
        return false;
    }

    public abstract void accept(TInstructionVisitor visitor);

    /**
     * Evaluate this instruction on the given constant inputs.
     * This is used for constant propagation
     *
     * @param sources the constant inputs, which are literals
     * @return the constant result, or null if the result is non-deterministic or non computable
     */
    public Object evaluate(Object[] sources) {
        return null;
    }
}
