package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.primitives.ModelEvent;

/**
 * Created by lauril on 8/25/16.
 */
public class EventSymbol extends SymbolWithScope {
    private final String modelVarName;
    private final ModelEvent event;

    public EventSymbol(String modelVarName, ModelEvent event) {
        super(modelVarName + "." + event);
        this.modelVarName = modelVarName;
        this.event = event;
    }

    public String getModelVarName() {
        return modelVarName;
    }

    public ModelEvent getEvent() {
        return event;
    }
}
