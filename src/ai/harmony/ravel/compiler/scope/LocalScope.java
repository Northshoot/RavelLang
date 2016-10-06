package ai.harmony.ravel.compiler.scope;

import ai.harmony.ravel.compiler.symbol.ReferenceSymbol;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, String> getReferencePairs() {
        List<Scope> nestedScopesOfType = super.getNestedScopesOfType(ReferenceSymbol.class);
        Map<String,String>  lst = new LinkedHashMap<>();

        for(Scope s: nestedScopesOfType){
            lst.put(((ReferenceSymbol)s).getName(), ((ReferenceSymbol)s).getValue());
        }
        return lst;
    }
}