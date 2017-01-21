package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class BinaryArithOp extends Instruction {
    private final int target;
    private final int src1;
    private final int src2;
    private final String op;

    public BinaryArithOp(ParserRuleContext definer, int target, int src1, int src2, String op) {
        super(definer);

        this.target = target;
        this.src1 = src1;
        this.src2 = src2;
        this.op = op;
    }

    public String toString() {
        return "binarith " + target + " = " + src1 + " " + op + " " + src2;
    }
}
