package edu.stanford.ravel.compiler.types;

/**
 * Similar to ControllerType, this is an empty type that is defined by a Space.
 * It should almost never appear in the compiler, except that ValidateScope complains
 * loudly if a TypeSymbol does not return a valid type.
 *
 * This way, every ComponentSymbol (controller or model) defines a type, and
 * every InstanceSymbol has a type.
 *
 * Created by gcampagn on 1/26/17.
 */
public class SpaceType implements Type {
    private final String name;

    public SpaceType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
