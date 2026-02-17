package ai.harmony.ravel.compiler.types;

import ai.harmony.ravel.compiler.symbol.Symbol;

/**
 * The type of a block that declares an interface or a view in Ravel code
 *
 * Created by gcampagn on 1/30/17.
 */
public class InterfaceType extends ClassType {
    public InterfaceType(Symbol symbol) {
        super(symbol.getName());
    }

    public void addMethod(String methodName, Type[] arguments, Type returnValue) {
        super.addMethod(methodName, arguments, returnValue);
    }

    public void addEvent(String eventName, Type[] arguments) {
        super.addEvent(eventName, arguments, false);
    }
}
