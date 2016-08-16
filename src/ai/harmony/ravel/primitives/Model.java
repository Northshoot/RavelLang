package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lauril on 7/21/16.
 */
public class Model extends Primitive {
    RavelParser.ModelDeclarationContext ctx;
    ModelType mModelType;
    String mName;
    private List<Field> mFields;
    private List<Property> mProperties;

    public Model(RavelParser.ModelDeclarationContext ctx){
        this.ctx = ctx;
        this.mName = ctx.NAME().toString();
        mModelType = new ModelType(ctx.modelType());
        System.out.println("Model type: " + mModelType);
        mFields = new LinkedList<>();
        //create fields from context
        mProperties = new LinkedList<>();
        //create properties from context
    }


}
