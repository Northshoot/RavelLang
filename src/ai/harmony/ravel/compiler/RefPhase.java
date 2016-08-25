package ai.harmony.ravel.compiler;

import ai.harmony.ravel.compiler.old.GlobalScope;
import ai.harmony.ravel.compiler.old.Scope;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by lauril on 8/19/16.
 */
public class RefPhase {
    ParseTreeProperty<Scope> scopes;
    GlobalScope globals;
    Scope currentScope; // resolve symbols starting in this scope


    public RefPhase(GlobalScope globals, ParseTreeProperty<Scope> scopes) {
        this.scopes = scopes;
        this.globals = globals;
    }
}
