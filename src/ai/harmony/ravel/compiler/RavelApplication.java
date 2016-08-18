package ai.harmony.ravel.compiler;

import ai.harmony.ravel.compiler.scopes.GlobalScope;
import ai.harmony.ravel.compiler.scopes.Scope;
import ai.harmony.ravel.compiler.symbols.BuiltInTypeSymbol;
import ai.harmony.ravel.compiler.symbols.PrimitiveSymbol;
import ai.harmony.ravel.primitives.FieldType;

/**
 * Created by lauril on 8/18/16.
 */
public class RavelApplication  {
    PrimitiveSymbol objectRoot;
    GlobalScope globals = new GlobalScope();

    public RavelApplication() { initTypeSystem(); }

    public void initTypeSystem(){
        //Build in types
        globals.define(new BuiltInTypeSymbol("integer"));
        globals.define(new BuiltInTypeSymbol("number"));
        globals.define(new BuiltInTypeSymbol("void")) ;

        //build in fields
        globals.define(new FieldType(FieldType.TYPE_BOOLEAN));
        globals.define(new FieldType(FieldType.TYPE_BYTE_ARRAY));
        globals.define(new FieldType(FieldType.TYPE_DATE));
        globals.define(new FieldType(FieldType.TYPE_DATE_TIME));
        globals.define(new FieldType(FieldType.TYPE_DOUBLE));
        globals.define(new FieldType(FieldType.TYPE_FLOAT));
        globals.define(new FieldType(FieldType.TYPE_INTEGER));
        globals.define(new FieldType(FieldType.TYPE_LONG));
        globals.define(new FieldType(FieldType.TYPE_STRING));

    }

    public String toString() { return globals.toString(); }
}
