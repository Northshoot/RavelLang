package edu.stanford.ravel.primitives;

/**
 * A name that will be bound by something in scope
 *
 * This is a special form of literal that is not quite literal but close enough.
 *
 * Created by gcampagn on 2/21/17.
 */
public class Reference {
    private final String value;

    public Reference(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
