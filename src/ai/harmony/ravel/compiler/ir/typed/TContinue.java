package ai.harmony.ravel.compiler.ir.typed;

/**
 * Created by gcampagn on 1/25/17.
 */
public class TContinue extends TInstruction {
    @Override
    public String toString() {
        return "continue";
    }

    public boolean affectsControlFlow() {
        return true;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
