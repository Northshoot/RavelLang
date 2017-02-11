package org.stanford.ravel.compiler.symbol;

/**
 * A symbol representing a collection of data like a struct or class.
 * Each member has a slot number indexed from 0 and we track data fields
 * and methods with different slot sequences. A ComponentSymbol
 * can also be a member of an aggregate itself (nested structs, ...).
 */
public abstract class ComponentSymbol extends SymbolWithScope implements TypeSymbol {
    ComponentSymbol(String name) {
        super(name);
    }
}