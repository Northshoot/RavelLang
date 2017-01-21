package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.Type;

/** This interface tags user-defined symbols that have static type information,
 *  like variables and functions.
 */
public interface TypedSymbol {
    Type getType();
    void setType(Type type);
}