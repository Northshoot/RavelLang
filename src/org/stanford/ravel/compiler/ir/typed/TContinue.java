package org.stanford.ravel.compiler.ir.typed;

/**
 * Created by gcampagn on 1/25/17.
 */
public class TContinue extends TInstruction {
    @Override
    public String toString() {
        return "continue";
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
