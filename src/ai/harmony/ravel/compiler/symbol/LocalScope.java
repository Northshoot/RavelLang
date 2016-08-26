package ai.harmony.ravel.compiler.symbol;

import ai.harmony.ravel.compiler.scope.BaseScope;
import ai.harmony.ravel.compiler.scope.Scope;

public class LocalScope extends BaseScope {

    String mName;
    public LocalScope(String name, Scope enclosingScope) {
        super(enclosingScope);
        this.mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }
}