package edu.stanford.ravel.compiler.ir.untyped;

import edu.stanford.ravel.compiler.ir.ComparisonOperation;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 1/23/17.
 */
public class ComparisonOp extends Instruction {
    public final int target;
    public final int src1;
    public final int src2;
    public final ComparisonOperation op;

    public ComparisonOp(ParserRuleContext definer, int target, int src1, int src2, ComparisonOperation op) {
        super(definer);
        assert op != null;

        this.target = target;
        this.src1 = src1;
        this.src2 = src2;
        this.op = op;
    }

    public String toString() {
        return "comp " + target + " = " + src1 + " " + op + " " + src2;
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
