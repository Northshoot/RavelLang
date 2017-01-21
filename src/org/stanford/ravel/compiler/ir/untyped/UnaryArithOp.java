package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class UnaryArithOp extends Instruction {
    private final int target;
    private final int source;
    private final String op;

    public UnaryArithOp(ParserRuleContext definer, int target, int source, String op) {
        super(definer);

        this.target = target;
        this.source = source;
        this.op = op;
    }

    public String toString() {
        return "unarith " + target + " = " + op + " " + source;
    }
}
