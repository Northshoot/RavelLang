package org.stanford.ravel.compiler.types;

import org.stanford.ravel.compiler.symbol.InterfaceSymbol;

/**
 * The type of a block that declares an interface in Ravel code
 *
 * Created by gcampagn on 1/30/17.
 */
public class InterfaceType extends ClassType {
    public InterfaceType(InterfaceSymbol symbol) {
        super(symbol.getName());
    }

    public void addMethod(String methodName, Type[] arguments, Type returnValue) {
        super.addMethod(methodName, arguments, returnValue);
    }

    public void addEvent(String eventName, Type[] arguments, Type returnValue) {
        super.addEvent(eventName, arguments, returnValue, false);
    }
}
