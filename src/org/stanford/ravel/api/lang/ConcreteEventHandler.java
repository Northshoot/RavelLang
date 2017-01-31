package org.stanford.ravel.api.lang;

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
    public final List<String> argumentNames;
    public final List<String> argumentTypes;

    public ConcreteEventHandler(String name, String model, List<String> argumentNames, List<String> argumentTypes, String code) {
        this.name = name;
        this.model = model;
        this.code = code;
        this.argumentNames = argumentNames;
        this.argumentTypes = argumentTypes;
    }
}
