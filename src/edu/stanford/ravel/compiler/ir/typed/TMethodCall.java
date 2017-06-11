package edu.stanford.ravel.compiler.ir.typed;

import edu.stanford.ravel.compiler.ir.Registers;
import edu.stanford.ravel.compiler.types.FunctionType;
import edu.stanford.ravel.compiler.types.ModelType;
import edu.stanford.ravel.compiler.types.Type;

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
        if (owner != Registers.VOID_REG) {
            int[] sources = new int[arguments.length + 1];
            sources[0] = owner;
            System.arraycopy(arguments, 0, sources, 1, arguments.length);
            return sources;
        } else {
            return arguments;
        }
    }

    @Override
    public Type[] getSourceTypes() {
        if (owner != Registers.VOID_REG) {
            Type[] argumentTypes = type.getArgumentTypes();
            Type[] sourceTypes = new Type[argumentTypes.length + 1];
            sourceTypes[0] = type.getOwner().getInstanceType();
            System.arraycopy(argumentTypes, 0, sourceTypes, 1, argumentTypes.length);
            return sourceTypes;
        } else {
            return type.getArgumentTypes();
        }
    }

    @Override
    public boolean readsMemory() {
        return true;
    }

    @Override
    public boolean writesMemory() {
        //TODO: kmethod calls need to be consolidated
        if (type.getOwner() instanceof ModelType) {
            switch (method) {
                case "create":
                case "get":
                case "iterator":
                case "first":
                case "last":
                case "size":
                    return false;

                case "save":
                case "clear":
                case "clearAll":
                case "delete":
                    return true;

                default:
                    throw new AssertionError("Unexpected method " + method + " in model");
            }
        } else if (type.getOwner() instanceof ModelType.IteratorType) {
            switch (method) {
                case "hasNext":
                    return false;

                case "next":
                    return true;

                default:
                    throw new AssertionError("Unexpected method " + method + " in iterator");
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
