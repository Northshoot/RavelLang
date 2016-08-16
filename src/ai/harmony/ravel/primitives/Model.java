package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lauril on 7/21/16.
 */
public class Model {
    RavelParser.ModelDeclarationContext ctx;
    int mModelType;
    private List<Field> mFields;
    private List<Property> mProperties;

    public Model(RavelParser.ModelDeclarationContext ctx){
        ctx = ctx;
        //mModelType = ctx.modelType()
        System.out.println("Model type: ctx.modelType()");
        mFields = new LinkedList<>();
        //create fields from context
        mProperties = new LinkedList<>();
        //create properties from context
    }


}
