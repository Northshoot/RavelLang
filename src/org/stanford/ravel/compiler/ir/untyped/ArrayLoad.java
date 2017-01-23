package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class ArrayLoad extends Instruction {
    public final int target;
    public final int source;
    public final int index;

    public ArrayLoad(ParserRuleContext definer, int target, int source, int index) {
        super(definer);

        this.target = target;
        this.source = source;
        this.index = index;
    }

    public String toString() {
        return "array.ld " + target + " = " + source + " [ " + index + " ]";
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
