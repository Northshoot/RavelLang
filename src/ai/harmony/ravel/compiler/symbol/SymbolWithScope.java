package ai.harmony.ravel.compiler.symbol;

import ai.harmony.ravel.compiler.Utils;
import ai.harmony.ravel.compiler.scope.BaseScope;
import ai.harmony.ravel.compiler.scope.Scope;

import java.util.Collections;
import java.util.List;

/** An abstract base class that houses common functionality for
 *  symbols like classes and functions that are both symbols and scopes.
 *  There is some common cut and paste functionality with {@link BaseSymbol}
 *  because of a lack of multiple inheritance in Java but it is minimal.
 */
public abstract class SymbolWithScope extends BaseScope implements Symbol, Scope {
    protected final String name; // All symbols at least have a name
    protected int index; 	// insertion order from 0; compilers often need this

    public SymbolWithScope(String name) {
        this.name = name;
    }

    @Override public String getName() { return name; }
    @Override public Scope getScope() { return enclosingScope; }
    @Override public void setScope(Scope scope) { setEnclosingScope(scope); }

    @Override public Scope getEnclosingScope() { return enclosingScope; }

    /** Return the name prefixed with the name of its enclosing scope
     *  using '.' (dot) as the scope separator.
     */
    public String getQualifiedName() {
        return enclosingScope.getName()+"."+name;
    }

    /** Return the name prefixed with the name of its enclosing scope. */
    public String getQualifiedName(String scopePathSeparator) {
        return enclosingScope.getName()+scopePathSeparator+name;
    }

    /** Return the fully qualified name includes all scopes from the root down
     *  to this particular symbol.
     */
    public String getFullyQualifiedName(String scopePathSeparator) {
        List<Scope> path = getEnclosingPathToRoot();
        Collections.reverse(path);
        return Utils.joinScopeNames(path, scopePathSeparator);
    }

    @Override
    public int getNumberOfSymbols() {
        return symbols.size();
    }

    @Override
    public boolean equals(Object obj) {
        if ( !(obj instanceof Symbol) ) {
            return false;
        }
        if ( obj==this ) {
            return true;
        }
        return name.equals(((Symbol)obj).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
