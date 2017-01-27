package org.stanford.ravel;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.symbol.FieldSymbol;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.primitives.Fields.*;
import org.stanford.ravel.primitives.Model;
import org.stanford.ravel.primitives.Variable;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 1/20/17.
 */
public class ModelIR {
    private final RavelCompiler driver;
    private final RavelApplication app;

    private Map<String, Model> mModels = new LinkedHashMap<>();

    public ModelIR(RavelCompiler driver, RavelApplication app) {
        this.driver = driver;
        this.app = app;
    }

    public void addModel(ModelSymbol ms){
        //LOGGER.info("Making model object from scope");
        //make concrete model
        String name = ms.getName();
        Model m = new Model(name, ms);
        app.addModel(name, m);

        // TODO finish the rest of the code here

//        LOGGER.log(Level.INFO, "Creating >>{0}<< model {1}", new Object[]{type, name});
        //TODO: not a clean solution
//        try{
//            List<RavelParser.ParamContext> component_parametersContext = ms.component_parameters().params().param();
//            for(RavelParser.ParamContext t: component_parametersContext){
//                m.addParam(t.getText());
//            }
//        } catch (NullPointerException e){
//            //no params so no worries
//        }
        Scope propScope = ms.getNestedScope("properties");
        List<VariableSymbol> prop = (List<VariableSymbol>) propScope.getAllSymbols();
        for (VariableSymbol s: prop) {
            Variable var = makeVariable(s);
            if(var == null){
                throw new IllegalArgumentException("Line: " + s.getDefNode().start.getLine()
                        + " Could not create variable " +s.toString());
            }
            m.setProperty(var);
        }
        // create fields for the schema
        Scope schemaScope = ms.getNestedScope("schema");
        List<FieldSymbol> schema = (List<FieldSymbol>) schemaScope.getAllSymbols();
        Iterator<FieldSymbol> s = schema.iterator();
        while (s.hasNext()) {
            Field f = makeField(m, s.next());
            m.addField(f);
        }//end field constructions
        System.out.println(m);
        mModels.put(m.getName(), m);
    }

    private Field makeField(Model m , FieldSymbol fs){
        boolean field_has_options = false;
        RavelParser.FieldDeclarationContext field_ctx = (RavelParser.FieldDeclarationContext) fs.getDefNode();
        //Field type name
        String field_type = field_ctx.field_type().getText();
        //get field type
        Field.Type ft = Field.getType(field_ctx.field_type().start.getType());

        Field.Builder f_concreate = null;
        //first we only create a specific field builder
        //and add leaf methods
        switch (ft) {
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
            case T_MODEL:
                f_concreate = new ModelField.Builder();
                break;
            case tINVALID:
                throw new RuntimeException("Could not instantiate field: " + field_type + " got tINVALID");

        }
        //Now we add generic field properties
        f_concreate
                .fieldType(ft) //set type
                .fieldTypeName(field_type) // give string value of type name
                .name(fs.getName()) // set name
                .model(m); //ad model to the field (for reverse name creation
        // now we add field options if any
        //            Field_optionsContext fieldOpt = null;//field_ctx.field_options();
        //
        //
        //            if (fieldOpt != null) {
        //                field_has_options = true;
        //                List<RavelParser.Field_optionContext> valp;
        //                valp = fieldOpt.field_option();
        //                Iterator<RavelParser.Field_optionContext> optPair = valp.iterator();
        //                String args = "";
        //                while (optPair.hasNext()) {
        //                    Field_optionContext el = optPair.next();
        //                    //This is assumes that field can not have expression
        //                    //TODO: needs redesign
        //                    String optName = "";//el.Identifier().getText();
        //                    String optValue = el.literal().getText();
        //                    args += optName + ":" + optValue + ",";
        //                    f_concreate.addOption(optName, optValue);
        //                }
        //                LOGGER.info("field arguments: [" + args + "]");
        //            }

        return f_concreate.build();
    }

    private Variable makeVariable(VariableSymbol vs) {
        Variable.Builder var = new Variable.Builder();
        var.name(vs.getName());
        RavelParser.PropValueContext propValueContext = ((RavelParser.VarAssignmentContext) vs.getDefNode()).propValue();

        //get class of next child, we know it only can be one
        if (propValueContext.literal() != null) {
            final RavelParser.LiteralContext literal = propValueContext.literal();
            try {
                String value = literal.boolean_rule().getText();
                var.stringType("boolean");
                return var.value(Boolean.parseBoolean(value)).build();
            } catch (NullPointerException e) { }
            try {
                String value = literal.number().integer().getText();
                var.stringType("integer");
                return var.value(Integer.parseInt(value)).build();
            } catch (NullPointerException e) { }
            try {
                String value = literal.number().float_point().getText();

                var.stringType("number");
                return var.value(Float.parseFloat(value)).build();
            } catch (NullPointerException e) { }
            try {
                String value = literal.STRING_LITERAL().getText();
                var.stringType("string");
                return var.value(value).build();
            } catch (NullPointerException e) { }
            try {
                String value = literal.STRING_LITERAL().getText();
                var.stringType("assignment");
                return var.value(value).build();
            } catch (NullPointerException e ) {}
            //try {
            //    String value = literal.Identifier().getText();
            //    var.stringType("variable");
            //    return var.value(value).build();
            //} catch (NullPointerException e ) {}
            //we could build and return here, but we need to be sure that parsing has identified
            //the right value.
            //merge prop and varAssignments
        } else if (propValueContext.propArray() != null){
            //either one has to be not null
            RavelParser.PropArrayContext propArrayContext = propValueContext.propArray();
            List<RavelParser.LiteralContext> prop = propArrayContext.literal();
            var.stringType("array");
            List<String> valList = new ArrayList<>();
            for(RavelParser.LiteralContext p: prop) valList.add(p.getText());
            return var.value(valList).build();
        } else {
            throw new RuntimeException("Illegal child when creating variable, expecting property or property array, got: " +
                    ((RavelParser.VarAssignmentContext) vs.getDefNode()).propValue().getChild(0).getClass().getName());
        }
        return null;
    }

    public Map<String, Model> getModels(){
        return this.mModels;
    }
}
