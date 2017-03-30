package edu.stanford.ravel.compiler.types;

/**
 * The type of "None"
 *
 * Created by gcampagn on 3/20/17.
 */
public class NullType implements Type {
    public static final Object NULL = new Object();
    public static final Type NULL_TYPE = new NullType();

    private NullType() {}

    @Override
    public String getName() {
        return "None";
    }
}
