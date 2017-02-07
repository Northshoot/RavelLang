package org.stanford.ravel.primitives;

import org.antlr.v4.misc.OrderedHashMap;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.EventType;

import java.util.List;
import java.util.Map;

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
}