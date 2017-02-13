package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/20/17.
 */
public class TIfStatement extends TInstruction {
    public int cond;
    public final TBlock iftrue;
    public final TBlock iffalse;

    public TIfStatement(int cond, TBlock iftrue, TBlock iffalse) {
        this.cond = cond;
        this.iftrue = iftrue;
        this.iffalse = iffalse;
    }

    public String toString() {
        return "if " + cond + " then B" + iftrue.getId() + " else B" + iffalse.getId();
    }

    @Override
    public int[] getSources() {
        return new int[]{ cond };
    }

    @Override
    public Type[] getSourceTypes() {
        return new Type[]{PrimitiveType.BOOL};
    }

    public boolean affectsControlFlow() {
        return true;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
