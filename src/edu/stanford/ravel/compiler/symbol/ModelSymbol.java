package edu.stanford.ravel.compiler.symbol;

import edu.stanford.ravel.compiler.scope.Scope;
import edu.stanford.ravel.compiler.types.ModelType;
import edu.stanford.ravel.primitives.Model;

/**
 * Created by lauril on 8/25/16.
 */
public class ModelSymbol extends ComponentSymbol implements TypeSymbol {
    private final ModelType mDefinedType;
    private final Model.Type mModelType;
    private Scope mSchemaScope;

    public ModelSymbol(String name, Model.Type mt) {
        super(name);

        // this is model type e.g.
        // local model assigned to embedded space
        // cannot  be accessed from any other space
        this.mModelType = mt;

        // this is the "type" of the model in the compiler sense
        // ie, a class type that has static methods create(), save()
        // and fields "record" and "error"
        mDefinedType = new ModelType(this, mt);
    }

    @Override
    public void nest(Scope scope) {
        super.nest(scope);
        if (scope.getName().equals("schema"))
            mSchemaScope = scope;
    }

    public void createModelType() {
        for (Symbol sym : mSchemaScope.getSymbols()) {
            assert sym instanceof FieldSymbol;
            mDefinedType.getRecordType().addField(sym.getName(), ((FieldSymbol) sym).getType());
        }
    }

    public Model.Type getModelType(){
        return this.mModelType;
    }

    @Override
    public ModelType getDefinedType() {
        return mDefinedType;
    }
}
