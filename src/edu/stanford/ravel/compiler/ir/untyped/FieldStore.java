package edu.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 1/20/17.
 */
public class FieldStore extends Instruction {
    public final int object;
    public final int value;
    public final String field;

    public FieldStore(ParserRuleContext definer, int object, String field, int value) {
        super(definer);

        this.object = object;
        this.field = field;
        this.value = value;
    }

    public String toString() {
        return "field.st " + object + " ." + field + " = " + value;
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
