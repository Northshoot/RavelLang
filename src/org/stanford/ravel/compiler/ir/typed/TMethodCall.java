package org.stanford.ravel.compiler.ir.typed;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.ir.untyped.InstructionVisitor;
import org.stanford.ravel.compiler.types.FunctionType;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TMethodCall extends TInstruction {
    private final FunctionType type;
    private final int owner;
    private final String method;
    private final int[] arguments;
    private final int target;

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
    int getSink() {
        return target;
    }

    @Override
    Type getSinkType() {
        return type.getReturnType();
    }

    @Override
    int[] getSources() {
        return arguments;
    }

    @Override
    Type[] getSourceTypes() {
        return type.getArgumentTypes();
    }

    @Override
    public boolean readsMemory() {
        return true;
    }

    @Override
    public boolean writesMemory() {
        return true;
    }
}
