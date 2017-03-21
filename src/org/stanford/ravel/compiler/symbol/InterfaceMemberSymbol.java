package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.ClassType;
import org.stanford.ravel.compiler.types.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * A member declaration in an interface block
 *
 * Created by gcampagn on 1/30/17.
 */
public class InterfaceMemberSymbol extends SymbolWithScope {
    private final boolean event;
    private final List<Type> arguments = new ArrayList<>();
    private final Type returnValue;

    public InterfaceMemberSymbol(String name, Type returnValue, boolean event) {
        super(name);

        this.returnValue = returnValue;
        this.event = event;
    }

    public Type getReturnValue() {
        return returnValue;
    }

    public Type[] getArguments() {
        return arguments.toArray(new Type[0]);
    }

    public boolean isEvent() {
        return event;
    }

    @Override
    public void define(Symbol sym) {
        super.define(sym);

        assert sym instanceof VariableSymbol;
        Type definedType = ((VariableSymbol) sym).getType();
        if (definedType instanceof ClassType)
            definedType = ((ClassType) definedType).getInstanceType();
        arguments.add(definedType);
    }
}
