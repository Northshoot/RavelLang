package org.stanford.ravel.compiler.types;

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
        //if (bound < 0)
        //    throw new IllegalArgumentException("Array size less than 0");
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

    public ArrayType makeImmutable() {
        return new ArrayType(elementType, bound, false);
    }

    @Override
    public String getName() {
        return elementType.getName() + (mutable ? "" : " const") + "[]";
    }

    @Override
    public boolean isAssignable(Type t) {
        return t instanceof ArrayType && elementType.equals(((ArrayType) t).elementType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayType arrayType = (ArrayType) o;

        if (mutable != arrayType.mutable) return false;
        return elementType.equals(arrayType.elementType);
    }

    @Override
    public int hashCode() {
        int result = (mutable ? 1 : 0);
        result = 31 * result + elementType.hashCode();
        return result;
    }

    @Override
    public boolean equalsExceptQualifiers(Type type) {
        if (type == null)
            return false;
        if (type.getClass().equals(this.getClass()))
            return false;

        ArrayType otherArray = (ArrayType)type;
        return elementType.equalsExceptQualifiers(otherArray.elementType);
    }
}
