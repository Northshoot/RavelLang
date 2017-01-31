package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.EventType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.ModelEvent;
import org.stanford.ravel.primitives.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lauril on 8/25/16.
 */
public class EventHandlerSymbol extends SymbolWithScope implements TypedSymbol {
    private final String modelVarName;
    private final List<VariableSymbol> arguments = new ArrayList<>();
    private EventType type;

    public EventHandlerSymbol(String modelVarName, String eventName) {
        super(modelVarName + "." + eventName);
        this.modelVarName = modelVarName;
    }

    public String getModelVarName() {
        return modelVarName;
    }

    @Override
    public void define(Symbol sym) {
        super.define(sym);

        // NOTE: we nest a block immediately inside the event handler,
        // hides all variable symbols from this call, so they don't in
        // fact appear as function argument names
        if (sym instanceof VariableSymbol) {
            assert ((VariableSymbol) sym).getType() != null;
            arguments.add((VariableSymbol)sym);
        }
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = (EventType) type;
    }

    public List<VariableSymbol> getArguments() {
        return Collections.unmodifiableList(arguments);
    }
}
