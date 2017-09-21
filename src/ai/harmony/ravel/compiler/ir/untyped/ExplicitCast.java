package ai.harmony.ravel.compiler.ir.untyped;

import ai.harmony.ravel.compiler.types.Type;
import org.antlr.v4.runtime.ParserRuleContext;

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
