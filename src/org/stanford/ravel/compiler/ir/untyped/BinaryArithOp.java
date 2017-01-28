package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.ir.BinaryOperation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class BinaryArithOp extends Instruction {
    public final int target;
    public final int src1;
    public final int src2;
    public final BinaryOperation op;

    public BinaryArithOp(ParserRuleContext definer, int target, int src1, int src2, BinaryOperation op) {
        super(definer);

        this.target = target;
        this.src1 = src1;
        this.src2 = src2;
        this.op = op;
    }

    public String toString() {
        return "binarith " + target + " = " + src1 + " " + op + " " + src2;
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
