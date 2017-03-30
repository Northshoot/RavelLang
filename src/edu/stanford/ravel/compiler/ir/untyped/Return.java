package edu.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 2/21/17.
 */
public class Return extends Instruction {
    public Return(ParserRuleContext definer) {
        super(definer);
    }

    @Override
    public String toString() {
        return "return";
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
