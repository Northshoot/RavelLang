package org.stanford.ravel.compiler.symbol;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.types.ControllerType;
import org.stanford.ravel.compiler.types.Type;

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

    public List<EventSymbol> getEvents(){
        return getSymbols().stream()
                .filter(s -> s instanceof EventSymbol)
                .map(s -> (EventSymbol) s)
                .collect(Collectors.toList());
    }

    public List<VariableSymbol> getParameters(){
        return getSymbols().stream()
                .filter(s -> s instanceof VariableSymbol)
                .map(s -> (VariableSymbol) s)
                .collect(Collectors.toList());
    }
}