package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class Move extends Instruction {
    public final int target;
    public final int source;

    public Move(ParserRuleContext definer, int target, int source) {
        super(definer);

        this.target = target;
        this.source = source;
    }

    public String toString() {
        return "move " + target + " = " + source;
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
