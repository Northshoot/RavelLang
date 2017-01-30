package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.EventType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.ModelEvent;

/**
 * Created by lauril on 8/25/16.
 */
public class EventHandlerSymbol extends SymbolWithScope implements TypedSymbol {
    private final String modelVarName;
    private EventType type;

    public EventHandlerSymbol(String modelVarName, String eventName) {
        super(modelVarName + "." + eventName);
        this.modelVarName = modelVarName;
    }

    public String getModelVarName() {
        return modelVarName;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = (EventType) type;
    }
}
