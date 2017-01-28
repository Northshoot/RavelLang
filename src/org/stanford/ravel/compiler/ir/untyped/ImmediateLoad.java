package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.antlr4.RavelParser;

import java.util.Collections;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class ImmediateLoad extends Instruction {
    public final int target;
    public final Object value;

    public ImmediateLoad(ParserRuleContext definer, int target, Object value) {
        super(definer);

        this.target = target;
        this.value = value;
    }

    public String toString() {
        return "loadimm " + target + " = '" + value + "'";
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
