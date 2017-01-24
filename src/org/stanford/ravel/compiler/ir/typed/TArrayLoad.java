package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.ArrayType;
import org.stanford.ravel.compiler.types.CompoundType;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/24/17.
 */
public class TArrayLoad extends TInstruction {
    private final Type type;
    private final ArrayType arrayType;
    private final int target;
    private final int source;
    private final int index;

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
    int getSink() {
        return target;
    }

    @Override
    Type getSinkType() {
        return type;
    }

    @Override
    int[] getSources() {
        return new int[] { source, index };
    }

    @Override
    Type[] getSourceTypes() {
        return new Type[] { arrayType, PrimitiveType.INT32 };
    }

    @Override
    public boolean readsMemory() {
        return true;
    }
}
