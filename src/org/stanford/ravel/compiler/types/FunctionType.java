package org.stanford.ravel.compiler.types;

/**
 * Created by gcampagn on 1/23/17.
 */
public class FunctionType implements Type {
    private final String name;
    private final ClassType owner;
    private final boolean isStatic;
    private final Type[] arguments;
    private final Type returnValue;

    FunctionType(String name, Type[] arguments, Type returnValue) {
        this.name = name;
        owner = null;
        isStatic = true;
        this.arguments = arguments;
        this.returnValue = returnValue;
    }

    FunctionType(String name, ClassType owner, boolean isStatic, Type[] arguments, Type returnValue) {
        this.name = name;
        this.owner = owner;
        this.isStatic = isStatic;
        this.arguments = arguments;
        this.returnValue = returnValue;
    }

    public ClassType getOwner() {
        return owner;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public Type getReturnType() {
        return returnValue;
    }
    public Type[] getArgumentTypes() {
        return arguments;
    }

    @Override
    public String getName() {
        return (owner != null ? owner.getName() + "." : "") + name;
    }
}
