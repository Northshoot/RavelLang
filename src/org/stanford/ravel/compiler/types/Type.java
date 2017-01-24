package org.stanford.ravel.compiler.types;

/** This interface is a tag that indicates the implementing object
 *  is a kind of type. Every type knows its name. In languages like C
 *  where we need a tree-like structure to represent a type, one
 *  could return a string representation as the name.
 *
 *  The types are typically things like struct or classes and primitive types,
 *  as well as the type trees used for languages like C.
 */
public interface Type {
    String getName();

    /**
     * Checks if @param other can be assigned to a variable of this type.
     * The default implementation just checks that the type is equal or is an error type
     *
     * @param other the type on the right hand side of the assignment
     * @return true if assignable, false otherwise.
     */
    default boolean isAssignable(Type other) {
        return this.equals(other) || other == PrimitiveType.ERROR;
    }
}
