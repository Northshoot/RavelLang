package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.ControllerType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lauril on 8/25/16.
 */
public class ControllerSymbol extends ComponentSymbol {
    private final ControllerType definedType;

    public ControllerSymbol(String name) {
        super(name);

        definedType = new ControllerType(name);
    }

    @Override
    public ControllerType getDefinedType() {
        return definedType;
    }

    public List<EventHandlerSymbol> getEvents(){
        return getSymbols().stream()
                .filter(s -> s instanceof EventHandlerSymbol)
                .map(s -> (EventHandlerSymbol) s)
                .collect(Collectors.toList());
    }

    public List<VariableSymbol> getParameters(){
        return getSymbols().stream()
                .filter(s -> s instanceof VariableSymbol)
                .map(s -> (VariableSymbol) s)
                .collect(Collectors.toList());
    }
}