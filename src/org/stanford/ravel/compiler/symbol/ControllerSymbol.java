package org.stanford.ravel.compiler.symbol;

import org.stanford.antlr4.RavelParser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lauril on 8/25/16.
 */
public class ControllerSymbol extends ComponentSymbol {

    public ControllerSymbol(String name) {
        super(name);
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