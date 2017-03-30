package edu.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 1/20/17.
 */
public class FieldLoad extends Instruction {
    public final int target;
    public final int source;
    public final String field;

    public FieldLoad(ParserRuleContext definer, int target, int source, String field) {
        super(definer);

        this.target = target;
        this.source = source;
        this.field = field;
    }

    public String toString() {
        return "field.ld " + target + " = " + source + " ." + field;
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
