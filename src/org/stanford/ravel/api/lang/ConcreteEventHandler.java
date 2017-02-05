package org.stanford.ravel.api.lang;

import org.stanford.ravel.compiler.symbol.VariableSymbol;

import java.util.List;

/**
 * A concrete, translated event handler function in the target language.
 *
 * Created by gcampagn on 1/27/17.
 */
public class ConcreteEventHandler {
    // All these are accessed from string templates
    public final String name;
    public final String model;
    public final String code;
    public final List<VariableSymbol> arguments;

    public ConcreteEventHandler(String name, String model, List<VariableSymbol> arguments, String code) {
        this.name = name;
        this.model = model;
        this.code = code;
        this.arguments = arguments;
    }
}
