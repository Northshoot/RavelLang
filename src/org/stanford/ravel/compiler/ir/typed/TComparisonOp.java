package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.ComparisonOperation;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TComparisonOp extends TInstruction {
    public final Type type;
    public int target;
    public int src1;
    public int src2;
    public final ComparisonOperation op;

    public TComparisonOp(Type type, int target, int src1, int src2, ComparisonOperation op) {
        assert op != null;

        this.type = type;
        this.target = target;
        this.src1 = src1;
        this.src2 = src2;
        this.op = op;
    }

    public String toString() {
        return "comp@" + type.getName() + " " + target + " = " + src1 + " " + op + " " + src2;
    }

    @Override
    public int[] getSources() {
        return new int[]{ src1, src2 };
    }

    @Override
    public int getSink() {
        return target;
    }

    @Override
    public Type[] getSourceTypes() {
        return new Type[]{ type, type };
    }

    @Override
    public Type getSinkType() {
        return PrimitiveType.BOOL;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
