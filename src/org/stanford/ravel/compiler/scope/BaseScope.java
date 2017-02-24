package org.stanford.ravel.compiler.scope;

/**
 * Created by lauril on 8/25/16.
 */
import org.stanford.ravel.compiler.Utils;
import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.symbol.ReferenceSymbol;
import org.stanford.ravel.compiler.symbol.Symbol;
import org.stanford.ravel.compiler.symbol.SymbolWithScope;

import java.util.*;
import java.util.stream.Collectors;

/** An abstract base class that houses common functionality for scopes. */
public abstract class BaseScope implements Scope {
    protected Scope enclosingScope = null; // null if this scope is the root of the scope tree
    private ParserRuleContext defNode; // points at definition node in tree
    /** All symbols defined in this scope; can include classes, functions,
     *  variables, or anything else that is a Symbol impl. It does NOT
     *  include non-Symbol-based things like LocalScope. See nestedScopes.
     */
    protected Map<String, Symbol> symbols = new LinkedHashMap<>();

    /** All directly contained scopes, typically LocalScopes within a
     *  LocalScope or a LocalScope within a FunctionSymbol. This does not
     *  include SymbolWithScope objects.
     */
    private List<Scope> nestedScopesNotSymbols = new ArrayList<>();
    private Map<String, Scope> nestedScopeMap = new HashMap<>();

    protected BaseScope() { }

    public Map<String, ? extends Symbol> getMembers() {
        return symbols;
    }

    @Override
    public Symbol getSymbol(String name) {
        return symbols.get(name);
    }

    @Override
    public void setEnclosingScope(Scope enclosingScope) {
        this.enclosingScope = enclosingScope;
    }

    public List<Scope> getAllNestedScopedSymbols() {
        List<Scope> scopes = new ArrayList<Scope>();
        Utils.getAllNestedScopedSymbols(this, scopes);
        return scopes;
    }

    @Override
    public Collection<Scope> getNestedScopedSymbols() {
        Set<Scope> output = new HashSet<>();
        for (Symbol x : getSymbols()) {
            if (x instanceof Scope) {
                output.add((Scope)x);
            }
        }
        return output;
    }

    @Override
    public List<Scope> getNestedScopes() {
        ArrayList<Scope> all = new ArrayList<>();
        all.addAll(getNestedScopedSymbols());
        all.addAll(nestedScopesNotSymbols);
        return all;
    }
    public void setDefNode(ParserRuleContext defNode) {
        this.defNode = defNode;
    }

    @Override
    public ParserRuleContext getDefNode() {
        return defNode;
    }

    /** Add a nested scope to this scope; could also be a FunctionSymbol
     *  if your language allows nested functions.
     */
    @Override
    public void nest(Scope scope) throws IllegalArgumentException {
        if ( scope instanceof SymbolWithScope) {
            throw new IllegalArgumentException("Add SymbolWithScope instance "+
                    scope.getName()+" via define()");
        }
        String scopeName = scope.getName();
        if(hasNestedScope(scopeName)) {
            throw new IllegalArgumentException("Nested scope " + scopeName +
                    " is already defined with symbols: " +getNestedScope(scopeName));
        }
        nestedScopesNotSymbols.add(scope);
        nestedScopeMap.put(scopeName,scope);
    }

    public Scope getNestedScope(String name){
        return nestedScopeMap.get(name);
    }

    public boolean hasNestedScope(String name){
        return nestedScopeMap.containsKey(name);
    }

    @Override
    public Symbol resolve(String name) {
        int dot = name.indexOf('.');
        if (dot >= 0) {
            String qualifier = name.substring(0, dot);
            String qualifiedname = name.substring(dot+1);
            Symbol scopeSym = resolve(qualifier);
            if (scopeSym != null) {
                if (!(scopeSym instanceof Scope))
                    return null;
                return ((Scope) scopeSym).resolve(qualifiedname);
            } else {
                // try resolving as a nested scope
                Scope pureScope = getEnclosingScope().getNestedScope(qualifier);
                if (pureScope == null)
                    return null;
                return pureScope.resolve(qualifiedname);
            }
        }

        Symbol s = symbols.get(name);
        if ( s!=null ) {
            return s;
        }
        // if not here, check any enclosing scope
        Scope parent = getEnclosingScope();
        if ( parent != null ) return parent.resolve(name);
        return null; // not found
    }

    public void define(Symbol sym) throws IllegalArgumentException {
        if ( symbols.containsKey(sym.getName()) ) {
            throw new IllegalArgumentException("duplicate symbol >>>"+sym.getName() + "<<<");
        }
        sym.setScope(this);
        symbols.put(sym.getName(), sym);
    }

    public Scope getEnclosingScope() { return enclosingScope; }

    /** Walk up enclosingScope until we find topmost. Note this is
     *  enclosing scope not necessarily parent. This will usually be
     *  a global scope or something, depending on your scope tree.
     */
    public Scope getOuterMostEnclosingScope() {
        Scope s = this;
        while ( s.getEnclosingScope()!=null ) {
            s = s.getEnclosingScope();
        }
        return s;
    }

    public List<Scope> getNestedScopesOfType(Class<?> type) {
        List<Scope> nestedScope = getAllNestedScopedSymbols();
        Iterator<Scope> iter = nestedScope.iterator();
        while (iter.hasNext()) {
            Scope s = iter.next();
            if (s.getClass()!=type)
                iter.remove();
                }

        return nestedScope;
    }


    @Override
    public List<Scope> getEnclosingPathToRoot() {
        List<Scope> scopes = new ArrayList<>();
        Scope s = this;
        while ( s!=null ) {
            scopes.add(s);
            s = s.getEnclosingScope();
        }
        return scopes;
    }

    @Override
    public Collection<Symbol> getSymbols() {
        return Collections.unmodifiableCollection(symbols.values());
    }

    public List<ReferenceSymbol> getRefenceSymbols(){
        return getSymbols().stream()
                .filter(s -> s instanceof ReferenceSymbol)
                .map(s -> (ReferenceSymbol) s)
                .collect(Collectors.toList());
    }
    public Collection<Symbol> getAllSymbols() {
        List<Symbol> syms = new ArrayList<>();
        syms.addAll(getSymbols());
        for (Symbol s : symbols.values()) {
            if ( s instanceof Scope ) {
                Scope scope = (Scope)s;
                syms.addAll(scope.getAllSymbols());
            }
        }
        for (Scope scope : nestedScopesNotSymbols)
            syms.addAll(scope.getAllSymbols());
        return syms;
    }

    @Override
    public int getNumberOfSymbols() {
        return symbols.size();
    }

    @Override
    public Set<String> getSymbolNames() {
        return symbols.keySet();
    }

    public String toString() { return symbols.keySet().toString(); }

    public String toScopeStackString(String separator) {
        return Utils.toScopeStackString(this, separator);
    }

    public String toQualifierString(String separator) {
        return Utils.toQualifierString(this, separator);
    }

    public String toTestString() {
        return toTestString(", ", ".");
    }

    public String toTestString(String separator, String scopePathSeparator) {
        Collection<Symbol> allSymbols = this.getAllSymbols();
        List<String> syms = Utils.map(allSymbols, s -> s.getScope().getName() + scopePathSeparator + s.getName());
        return Utils.join(syms, separator);
    }
}
