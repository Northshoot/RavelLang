package ai.harmony.ravel.compiler.symbols;

/**
 * Created by lauril on 8/18/16.
 */

import ai.harmony.ravel.primitives.Type;

/** A symbol to represent built in types such int, float primitive types */
public class BuiltInTypeSymbol extends Symbol implements Type {
        public BuiltInTypeSymbol(String name) { super(name); }
    }
