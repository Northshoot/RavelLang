package org.stanford.ravel.compiler.types;

import java.lang.reflect.Array;

/**
 * Created by gcampagn on 1/24/17.
 */
public class ArrayType implements Type {
    private final static int UNBOUNDED = -1;

    private final int bound;
    private final boolean mutable;
    private final Type elementType;

    public ArrayType(Type elementType) {
        this(elementType, UNBOUNDED, true);
    }

    public ArrayType(Type elementType, int bound) {
        this(elementType, bound, true);
    }

    private ArrayType(Type elementType, int bound, boolean mutable) {
        if (bound < 0)
            throw new IllegalArgumentException("Array size less than 0");
        this.bound = bound;
        this.mutable = mutable;
        this.elementType = elementType;
    }

    public Type getElementType() {
        return elementType;
    }

    public boolean isMutable() {
        return mutable;
    }

    public boolean isKnownBound() {
        return bound != UNBOUNDED;
    }

    public int getBound() {
        return bound;
    }

    @Override
    public String getName() {
        return elementType.getName() + "[]";
    }

    @Override
    public boolean isAssignable(Type t) {
        return t instanceof ArrayType && elementType.equals(((ArrayType) t).elementType);
    }
}
