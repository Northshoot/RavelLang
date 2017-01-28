package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.ArrayType;
import org.stanford.ravel.compiler.types.CompoundType;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/24/17.
 */
public class TArrayStore extends TInstruction {
    public final Type type;
    public final ArrayType arrayType;
    public final int object;
    public final int index;
    public final int value;

    public TArrayStore(Type type, ArrayType arrayType, int object, int index, int value) {
        this.type = type;
        this.arrayType = arrayType;
        this.object = object;
        this.index = index;
        this.value = value;
    }

    public String toString() {
        return "field.st@" + type.getName() + " " + object + " [ " + index + " ] = " + value;
    }

    @Override
    int[] getSources() {
        return new int[] { object, index, value };
    }

    @Override
    Type[] getSourceTypes() {
        return new Type[] { arrayType, PrimitiveType.INT32, type };
    }

    @Override
    public boolean writesMemory() {
        return true;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
