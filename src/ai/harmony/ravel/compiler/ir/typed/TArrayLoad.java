package ai.harmony.ravel.compiler.ir.typed;

import ai.harmony.ravel.compiler.types.ArrayType;
import ai.harmony.ravel.compiler.types.PrimitiveType;
import ai.harmony.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/24/17.
 */
public class TArrayLoad extends TInstruction {
    public final Type type;
    public final ArrayType arrayType;
    public int target;
    public int source;
    public int index;

    public TArrayLoad(Type type, ArrayType arrayType, int target, int source, int index) {
        this.type = type;
        this.arrayType = arrayType;
        this.target = target;
        this.source = source;
        this.index = index;
    }

    public String toString() {
        return "array.ld@" + arrayType.getName() +" " + target + " = " + source + " [ " + index + " ]";
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
        return new int[] { source, index };
    }

    @Override
    public Type[] getSourceTypes() {
        return new Type[] { arrayType, PrimitiveType.INT32 };
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
        return ((Object[])args[0])[(int)args[1]];
    }

}
