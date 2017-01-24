package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.Type;

/**
 * A symbol that defines a type.
 * Could be a ModelSymbol (which defines a ModelType), or a PrimitiveTypeSymbol
 *
 * Created by gcampagn on 1/20/17.
 */
public interface TypeSymbol {
    Type getDefinedType();
}
