package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.InterfaceSymbol;
import org.stanford.ravel.compiler.types.Type;

import java.util.Map;

/**
 * Created by gcampagn on 1/30/17.
 */
public class Interface extends Primitive {
    private final InterfaceSymbol symbol;

    public Interface(String name, InterfaceSymbol symbol) {
        super(name, name + "Interface");
        this.symbol = symbol;
    }

    public InstantiatedInterface instantiate(Space space, Map<String, Object> parameters, String varName) {
        InstantiatedInterface instantiated = new InstantiatedInterface(space, this, varName);
        instantiated.setManyParam(parameters);
        // TODO: check types of parameters
        // TODO: check that all parameters are set
        return instantiated;
    }

    public Type getType() {
        return symbol.getDefinedType();
    }
}
