package ai.harmony.ravel.compiler.ir.typed;

/**
 * Created by gcampagn on 2/21/17.
 */
public class TReturn extends TInstruction {
    @Override
    public String toString() {
        return "return";
    }

    public boolean affectsControlFlow() {
        return true;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
