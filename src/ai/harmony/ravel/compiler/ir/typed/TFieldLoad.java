package ai.harmony.ravel.compiler.ir.typed;

import ai.harmony.ravel.compiler.types.ArrayType;
import ai.harmony.ravel.compiler.types.CompoundType;
import ai.harmony.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TFieldLoad extends TInstruction {
    public final Type type;
    public final CompoundType compoundType;
    public int target;
    public int source;
    public final String field;

    public TFieldLoad(Type type, CompoundType compoundType, int target, int source, String field) {
        this.type = type;
        this.compoundType = compoundType;
        this.target = target;
        this.source = source;
        this.field = field;
    }

    public String toString() {
        return "field.ld@" + type.getName() +" " + target + " = " + source + " ." + field;
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
    public int[] getSources() {
        return new int[] { source };
    }

    @Override
    public Type[] getSourceTypes() {
        return new Type[] { compoundType };
    }

    @Override
    public boolean readsMemory() {
        return true;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluate(Object[] args) {
        if (type instanceof ArrayType) {
            return ((Object[])args[0]).length;
        } else {
            return null;
        }
    }

}
