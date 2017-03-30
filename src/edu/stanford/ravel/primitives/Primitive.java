package edu.stanford.ravel.primitives;

/**
 * Created by lauril on 7/21/16.
 */
public abstract class Primitive {
    private final String name;

    public Primitive(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
