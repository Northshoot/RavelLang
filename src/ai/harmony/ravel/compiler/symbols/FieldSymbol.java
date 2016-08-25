package ai.harmony.ravel.compiler.symbols;

/**
 * Created by lauril on 8/19/16.
 */
public class FieldSymbol extends Symbol  {

    String mTypeName;
    public FieldSymbol( String name,  String typeName, Symbol.Type type) {
        super(name, type);
        this.mTypeName = typeName;

    }
}
