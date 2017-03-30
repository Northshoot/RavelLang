package edu.stanford.ravel.compiler.ir.typed;

/**
 * Created by gcampagn on 1/25/17.
 */
public class TBreak extends TInstruction {
    @Override
    public String toString() {
        return "break";
    }

    public boolean affectsControlFlow() {
        return true;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
