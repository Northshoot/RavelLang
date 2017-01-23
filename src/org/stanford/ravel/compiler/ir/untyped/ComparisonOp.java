package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.ir.BinaryOperation;
import org.stanford.ravel.compiler.ir.ComparisonOperation;

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
