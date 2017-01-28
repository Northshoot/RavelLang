package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 1/20/17.
 */
public class WhileLoop extends Instruction {
    public final int cond;
    public final Block head;
    public final Block body;

    public WhileLoop(ParserRuleContext definer, int cond, Block head, Block body) {
        super(definer);

        this.cond = cond;
        this.head = head;
        this.body = body;
    }

    public String toString() {
        return "while (\n" + head + ") " + cond + " {\n" + body + "}";
    }

    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
