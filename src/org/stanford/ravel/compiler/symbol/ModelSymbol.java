package org.stanford.ravel.compiler.symbol;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.types.CompoundType;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        mDefinedType = new ModelType(this);
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
    public Type getDefinedType() {
        return mDefinedType;
    }
}
