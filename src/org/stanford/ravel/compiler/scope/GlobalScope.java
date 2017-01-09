package org.stanford.ravel.compiler.scope;

/**
 * Created by lauril on 8/25/16.
 */
public class GlobalScope extends BaseScope {
    public GlobalScope(Scope scope) { super(scope); }
    public String getName() { return "global"; }

    public String toString() { return "Global Symbol"; }
}
