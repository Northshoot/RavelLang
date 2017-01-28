package org.stanford.ravel.compiler;

import org.stanford.ravel.compiler.scope.GlobalScope;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.symbol.PrimitiveTypeSymbol;
import org.stanford.ravel.compiler.symbol.Symbol;
import org.stanford.ravel.compiler.symbol.TypeSymbol;
import org.stanford.ravel.compiler.symbol.TypedSymbol;

/**
 * Created by gcampagn on 1/26/17.
 */
public class ValidateScope {
    private ValidateScope() {}

    private static void validate(Scope scope, Symbol symbol) {
        assert symbol.getName() != null;
        // all symbols should correspond to some AST node
        // with the exception of primitive types, which just "exist"
        // (they are defined by GlobalScope during construction)
        assert symbol.getDefNode() != null || symbol instanceof PrimitiveTypeSymbol;
        assert symbol.getScope() == scope;

        if (symbol instanceof TypeSymbol)
            assert ((TypeSymbol) symbol).getDefinedType() != null;
        if (symbol instanceof TypedSymbol)
            assert ((TypedSymbol) symbol).getType() != null;
        if (symbol instanceof Scope)
            validate(scope, (Scope)symbol);
    }

    private static void validate(Scope parent, Scope scope) {
        assert scope.getName() != null;
        assert scope.getDefNode() != null;
        assert scope.getEnclosingScope() == parent;

        for (Symbol sym : scope.getSymbols()) {
            validate(scope, sym);
        }
        for (Scope sub : scope.getNestedScopes()) {
            if (sub instanceof Symbol)
                continue;
            validate(scope, sub);
        }
    }

    public static void validate(GlobalScope scope) {
        validate(null, scope);
    }
}
