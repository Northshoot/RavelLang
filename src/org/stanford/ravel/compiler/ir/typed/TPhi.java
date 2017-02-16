package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.Type;

import java.util.Arrays;

/**
 * A Phi node
 *
 * Created by gcampagn on 2/8/17.
 */
public class TPhi extends TInstruction {
    public final Type type;
    public final int target;
    public final int[] sources;
    public final TBlock[] blocks;

    public TPhi(Type type, int target, int[] sources, TBlock[] blocks) {
        assert sources.length == blocks.length;

        this.type = type;
        this.target = target;
        this.sources = sources;
        this.blocks = blocks;
    }

    public String toString() {
        String str =  "phi@" + type.getName() + " " + target + " = phi(";
        for (int i = 0; i < sources.length; i++) {
            int src = sources[i];
            TBlock block = blocks[i];
            str += " " + src + " from B" + block.getId() + ", ";
        }
        return str + ")";
    }

    @Override
    public int[] getSources() {
        return sources;
    }

    @Override
    public int getSink() {
        return target;
    }

    @Override
    public Type[] getSourceTypes() {
        Type[] types = new Type[sources.length];
        Arrays.fill(types, type);
        return types;
    }

    public TBlock[] getSourceBlocks() {
        return blocks;
    }

    @Override
    public Type getSinkType() {
        return type;
    }

    public Type getPhiType() {
        return type;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluate(Object[] args) {
        Object res = args[0];
        for (Object o : args) {
            if (!o.equals(res))
                return null;
        }
        return res;
    }

}
