package org.stanford.ravel.primitives;

import org.antlr.v4.misc.OrderedHashMap;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;

import java.util.Map;

/**
 * Returns event hooks in various languages
 * translates loops and access to the variables
 * Created by lauril on 8/16/16.
 */
public class Event {
    private final VariableSymbol modelVar;
    private final ModelEvent event;
    private final TypedIR body;

    public Event(VariableSymbol modelVar, ModelEvent event, TypedIR body) {
        this.modelVar = modelVar;
        this.event = event;
        this.body = body;
    }

    public VariableSymbol getModelVar() {
        return modelVar;
    }

    @Override
    public String toString() {
        return "Event " + modelVar.getName() + "." + event;
    }
}
