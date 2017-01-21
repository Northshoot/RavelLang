package org.stanford.ravel.compiler.types;

/**
 * Created by gcampagn on 1/20/17.
 */
public enum PrimitiveType implements Type {
    ANY, ERROR, INT32, DOUBLE, BOOL, STR;

    public String getName() {
        return name().toLowerCase();
    }

    public boolean isValid() {
        return this != ANY && this != ERROR;
    }
}
