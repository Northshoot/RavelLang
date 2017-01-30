package org.stanford.ravel.api.lang;

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

    public ConcreteEventHandler(String name, String model, String code) {
        this.name = name;
        this.model = model;
        this.code = code;
    }
}
