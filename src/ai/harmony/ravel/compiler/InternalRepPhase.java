package ai.harmony.ravel.compiler;

import ai.harmony.ravel.RavelApplication;
import ai.harmony.ravel.antlr4.RavelBaseListener;
import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.scope.GlobalScope;
import ai.harmony.ravel.compiler.scope.Scope;
import ai.harmony.ravel.compiler.symbol.*;
import ai.harmony.ravel.primitives.Fields.*;
import ai.harmony.ravel.primitives.Fields.Field.*;
import ai.harmony.ravel.primitives.Model;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;

/**
 * Created by lauril on 8/30/16.
 */
public class InternalRepPhase extends RavelBaseListener {
    GlobalScope globals;
    Scope currentScope;
    RavelApplication rApp;
    String field_path = "ai.harmony.ravel.primitives.Fields.";

    public InternalRepPhase(GlobalScope globals, RavelApplication rApp) {
        this.globals = globals;
        this.rApp = rApp;
        currentScope = globals;
    }

    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        //first instantiate components
    }

    @Override
    public void enterModelScope(RavelParser.ModelScopeContext ctx) {
        //Create models and fields set properties, add to
        String name = ctx.Identifier().getText();
        String type = ctx.modelType().getText();
        Model m = new Model(name, Model.getType(type));
        //TODO: not a clean solution
        System.out.println(ctx.scope.getName());
        Scope propScope = ctx.scope.getNestedScope("properties");
        List<VariableSymbol> prop = (List<VariableSymbol>) propScope.getAllSymbols();
        Iterator<VariableSymbol> p = prop.iterator();
        while(p.hasNext()){
            VariableSymbol vs = p.next();
            m.setProperty(vs.getName(), vs.getValue());

        }
        // create fields for the schema
        Scope schemaScope = ctx.scope.getNestedScope("schema");
        List<FieldSymbol> schema = (List<FieldSymbol>) schemaScope.getAllSymbols();
        Iterator<FieldSymbol> s = schema.iterator();
        while(s.hasNext()){
            FieldSymbol fs = s.next();
            RavelParser.FieldDeclarationContext field_ctx = (RavelParser.FieldDeclarationContext) fs.getDefNode();
            //Field type name
            String field_type = field_ctx.field_type().getText();
            //get field type
            Field.Type ft = Field.getType(field_ctx.field_type().start.getType());
            //deal with parameters
            RavelParser.ElementValuePairsContext elementValuePairContext = field_ctx.elementValuePairs();
            Map<String, String> field_args = null;

            if( elementValuePairContext != null ) {
                field_args = new HashMap<>();
                List<RavelParser.ElementValuePairContext> valp;
                valp = elementValuePairContext.elementValuePair();
                Iterator<RavelParser.ElementValuePairContext> elementpair = valp.iterator();
                while ( elementpair.hasNext() ) {
                    RavelParser.ElementValuePairContext el = elementpair.next();
                    //This is assums that field can not have expression
                    //TODO: needs redesign
                    field_args.put(el.Identifier().getText(), el.elementValue().getText());
                }
            }

            Builder f_concreate  = null;
            //first we only create a specific field builder
            //and add leaf methods
            switch ( ft ){
                case T_INTEGER:
                    f_concreate = new IntegerField.Builder();
                    break;
                case T_NUMBER:
                    f_concreate = new NumberField.Builder();
                    break;
                case T_DATE:
                    f_concreate = new DateField.Builder();
                    break;
                case T_DATE_TIME:
                    f_concreate = new DateTimeField.Builder();
                    break;
                case T_TIME_STAMP:
                    f_concreate = new TimeStampField.Builder();
                    break;
                case T_BYTE:
                    f_concreate = new ByteField.Builder();
                    break;
                case T_STRING:
                    f_concreate = new StringField.Builder();
                    break;
                case T_BOOLEAN:
                    f_concreate = new BooleanField.Builder();
                    break;
                case T_CONTEXT:
                    f_concreate = new ContextField.Builder();
                    break;
                case tINVALID:
                    throw new RuntimeException("Could not instantiate field: " + field_type + " got tINVALID");

            }
            //Now we add generic field properties
            f_concreate
                    .fieldType(ft)
                    .fieldTypeName(field_type)
                    .name(fs.getName())
                    .model(m)
                    .documentation(field_args.getOrDefault("documentation", "Field or type " + field_type));

            m.addField(fs.getName(), f_concreate.build());
            rApp.addModel(m.getName(), m);


        }

    }

    @Override
    public void enterControllerScope(RavelParser.ControllerScopeContext ctx) {
        //create controllers

    }

    @Override
    public void enterSpaceScope(RavelParser.SpaceScopeContext ctx) {

    }
}
