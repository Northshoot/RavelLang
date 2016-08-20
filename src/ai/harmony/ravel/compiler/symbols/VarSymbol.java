package ai.harmony.ravel.compiler.symbols;

import ai.harmony.ravel.compiler.scopes.Scope;

/**
 * Created by lauril on 8/19/16.
 */
public class VarSymbol extends Symbol  {
    String mTypeName;


    public VarSymbol(String varType, String name,  Symbol.Type type) {
        super(name, type);
        this.mTypeName = varType;
    }

}
