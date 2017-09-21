package ai.harmony.ravel.compiler.ir.typed;

import ai.harmony.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/20/17.
 */
public class TImmediateLoad extends TInstruction {
    public int target;
    public final Object value;
    public final Type type;

    public TImmediateLoad(Type type, int target, Object value) {
        this.type = type;
        this.target = target;
        this.value = value;
    }

    public String toString() {
        return "loadimm@" + type.getName() + " " + target + " = '" + value + "'";
    }

    @Override
    public int getSink() {
        return target;
    }

    @Override
    public Type getSinkType() {
        return type;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluate(Object[] args) {
        return value;
    }

}
