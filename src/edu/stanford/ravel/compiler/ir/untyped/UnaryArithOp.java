package edu.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;
import edu.stanford.ravel.compiler.ir.UnaryOperation;

/**
 * Created by gcampagn on 1/20/17.
 */
public class UnaryArithOp extends Instruction {
    public final int target;
    public final int source;
    public final UnaryOperation op;

    public UnaryArithOp(ParserRuleContext definer, int target, int source, UnaryOperation op) {
        super(definer);

        this.target = target;
        this.source = source;
        this.op = op;
    }

    public String toString() {
        return "unarith " + target + " = " + op + " " + source;
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
