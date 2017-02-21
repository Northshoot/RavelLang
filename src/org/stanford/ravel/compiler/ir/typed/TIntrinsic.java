package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.types.Type;

/**
 * An intrinsic operation, which is a kind of builtin function call
 * with special meaning in the backend
 *
 * This is a catch-all instruction that is introduced by lowering passes
 * and the security transformation later in the process.
 *
 * Intrinsic must be pure and cannot read or write memory.
 *
 * Created by gcampagn on 2/21/17.
 */
public class TIntrinsic extends TInstruction {
    public final Type returnType;
    public final Type[] argumentTypes;
    public final String name;
    public final int[] arguments;
    public int target;

    public TIntrinsic(Type returnType, Type[] argumentTypes, int target, String intrinsic, int[] arguments) {
        this.returnType = returnType;
        this.argumentTypes = argumentTypes;
        this.target = target;
        this.name = intrinsic;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        String str = "instrinsic@" + returnType.getName() + " " + target + " = " + name + " (";
        for (int arg : arguments) {
            str += " " + arg + ", ";
        }
        return str + ")";
    }

    @Override
    public int getSink() {
        return target;
    }

    @Override
    public Type getSinkType() {
        return returnType;
    }

    @Override
    public int[] getSources() {
        return arguments;
    }

    @Override
    public Type[] getSourceTypes() {
        return argumentTypes;
    }

    @Override
    public boolean readsMemory() {
        return false;
    }

    @Override
    public boolean writesMemory() {
        return false;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluate(Object[] args) {
        switch (name) {
            // do something with well-known intrinsics

            default:
                return null;
        }
    }
}
