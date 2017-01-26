package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 1/25/17.
 */
public class Continue extends Instruction {
    public Continue(ParserRuleContext definer) {
        super(definer);
    }

    @Override
    public String toString() {
        return "continue";
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
