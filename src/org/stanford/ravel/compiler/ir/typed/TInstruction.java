package org.stanford.ravel.compiler.ir.typed;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

import static org.stanford.ravel.compiler.ir.Registers.VOID_REG;

/**
 * Created by gcampagn on 1/20/17.
 */
public abstract class TInstruction {
    int[] getSources() {
        return new int[0];
    }
    Type[] getSourceTypes() {
        return new Type[0];
    }

    int getSink() {
        return VOID_REG;
    }

    Type getSinkType() {
        return PrimitiveType.VOID;
    }

    boolean writesMemory() {
        return false;
    }

    boolean readsMemory() {
        return false;
    }

    public abstract void accept(TInstructionVisitor visitor);
}
