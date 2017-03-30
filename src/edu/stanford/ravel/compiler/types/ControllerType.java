package edu.stanford.ravel.compiler.types;

/**
 * An empty type that is defined by a Controller.
 *
 * This way, every ComponentSymbol (controller or model) defines a type, and
 * every InstanceSymbol has a type.
 *
 * Created by gcampagn on 1/26/17.
 */
public class ControllerType implements Type {
    private final String name;

    public ControllerType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
