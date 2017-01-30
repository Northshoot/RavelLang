package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.EventType;
import org.stanford.ravel.compiler.types.FunctionType;
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

    Type getReturnValue() {
        return returnValue;
    }

    Type[] getArguments() {
        return arguments.toArray(new Type[0]);
    }

    boolean isEvent() {
        return event;
    }

    @Override
    public void define(Symbol sym) {
        super.define(sym);

        assert sym instanceof VariableSymbol;
        arguments.add(((VariableSymbol) sym).getType());
    }
}
