package ai.harmony.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 1/20/17.
 */
public class ArrayStore extends Instruction {
    public final int object;
    public final int value;
    public final int index;

    public ArrayStore(ParserRuleContext definer, int object, int index, int value) {
        super(definer);

        this.object = object;
        this.index = index;
        this.value = value;
    }

    public String toString() {
        return "field.st " + object + " [ " + index + " ] = " + value;
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
