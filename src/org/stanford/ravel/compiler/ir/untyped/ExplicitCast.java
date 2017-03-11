package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 3/10/17.
 */
public class ExplicitCast  extends Instruction {
    public final int target;
    public final int source;
    public final Type targetType;

    public ExplicitCast(ParserRuleContext definer, int target, int source, Type targetType) {
        super(definer);

        this.target = target;
        this.source = source;
        this.targetType = targetType;
    }

    public String toString() {
        return "cast " + target + " = (" + targetType.getName() + ") " + source;
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
