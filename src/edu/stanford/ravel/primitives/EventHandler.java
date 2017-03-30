package edu.stanford.ravel.primitives;

import edu.stanford.ravel.compiler.types.EventType;
import edu.stanford.ravel.compiler.ir.typed.TypedIR;
import edu.stanford.ravel.compiler.symbol.VariableSymbol;

import java.util.Collection;
import java.util.List;

/**
 * Returns event hooks in various languages
 * translates loops and access to the variables
 * Created by lauril on 8/16/16.
 */
public class EventHandler {
    private final VariableSymbol modelVar;
    private final List<VariableSymbol> arguments;
    private final EventType event;
    private final TypedIR body;

    public EventHandler(VariableSymbol modelVar, List<VariableSymbol> arguments, EventType event, TypedIR body) {
        this.modelVar = modelVar;
        this.arguments = arguments;
        this.event = event;
        this.body = body;
    }

    public VariableSymbol getModelVar() {
        return modelVar;
    }

    public EventType getEventType() {
        return event;
    }
    public String getEventName() {
        return event.getEventName();
    }

    public TypedIR getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Event " + modelVar.getName() + "." + getEventName();
    }

    public List<VariableSymbol> getArguments() {
        return arguments;
    }

    public Collection<Integer> getVariables() {
        return body.getVariables();
    }
}
