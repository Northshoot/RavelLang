package ai.harmony.ravel.compiler.scopes;

/**
 * Created by lauril on 8/18/16.
 */

import ai.harmony.ravel.compiler.symbols.Symbol;
import ai.harmony.ravel.primitives.Primitive;

/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
 ***/
public interface Scope {
     String getScopeName();

    /** Where to look next for symbols; superclass or enclosing scope */
    Scope getParentScope();
    /** Scope in which this scope defined. For global scope, it's null */
    Scope getEnclosingScope();

    /** Define a symbol in the current scope */
    void define(Symbol sym);

    Primitive getPrimitive();

    /** Look up name in this scope or in parent scope if not here */
    Symbol resolve(String name);
}
