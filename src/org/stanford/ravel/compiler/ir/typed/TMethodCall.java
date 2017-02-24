package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.types.FunctionType;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TMethodCall extends TInstruction {
    public final FunctionType type;
    public int owner;
    public final String method;
    public final int[] arguments;
    public int target;

    public TMethodCall(FunctionType type, int target, int owner, String method, int[] arguments) {
        this.type = type;
        this.target = target;
        this.owner = owner;
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        String str = "call@" + type.getName() + " " + target + " = " + (owner != Registers.VOID_REG ? owner + " ." : "") + method + " (";
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
        return type.getReturnType();
    }

    @Override
    public int[] getSources() {
        return arguments;
    }

    @Override
    public Type[] getSourceTypes() {
        return type.getArgumentTypes();
    }

    @Override
    public boolean readsMemory() {
        return true;
    }

    @Override
    public boolean writesMemory() {
        if (type.getOwner() instanceof ModelType) {
            switch (method) {
                case "create":
                case "get":
                case "all":
                case "first":
                case "last":
                case "size":
                    return false;

                case "save":
                case "clear":
                    return true;

                default:
                    throw new AssertionError("Unexpected method " + method + " in model");
            }
        } else {
            return true;
        }
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
