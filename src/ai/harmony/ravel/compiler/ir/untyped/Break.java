package ai.harmony.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 1/25/17.
 */
public class Break extends Instruction {
    public Break(ParserRuleContext definer) {
        super(definer);
    }

    @Override
    public String toString() {
        return "break";
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
