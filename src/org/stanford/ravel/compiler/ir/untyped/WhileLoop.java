package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;

import static org.stanford.ravel.compiler.ir.untyped.UntypedIR.VOID_REG;

/**
 * Created by gcampagn on 1/20/17.
 */
public class WhileLoop extends Instruction {
    private final int cond;
    private final Block body;

    public WhileLoop(ParserRuleContext definer, int cond, Block body) {
        super(definer);

        this.cond = cond;
        this.body = body;
    }

    public String toString() {
        return "while " + cond + " {\n" + body + "}";
    }

    void accept(InstructionVisitor visitor) {
        super.accept(visitor);
        body.accept(visitor);
    }
}
