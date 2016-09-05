package ai.harmony.ravel.compiler;

import ai.harmony.ravel.RavelApplication;
import ai.harmony.ravel.antlr4.RavelBaseListener;
import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.antlr4.RavelParser.VarAssigmentContext;
import ai.harmony.ravel.compiler.scope.GlobalScope;
import ai.harmony.ravel.compiler.scope.Scope;
import ai.harmony.ravel.compiler.symbol.*;
import ai.harmony.ravel.primitives.Controller;
import ai.harmony.ravel.primitives.Event;
import ai.harmony.ravel.primitives.Fields.*;
import ai.harmony.ravel.primitives.Fields.Field.Builder;
import ai.harmony.ravel.primitives.Model;
import ai.harmony.ravel.primitives.Variable;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by lauril on 8/30/16.
 */
public class InternalRepPhase extends RavelBaseListener {
    private static Logger LOGGER = Logger.getLogger(InternalRepPhase.class.getName());
    private GlobalScope globals;
    private Scope currentScope;
    private RavelApplication rApp;

    public InternalRepPhase(GlobalScope globals, RavelApplication rApp) {
        this.globals = globals;
        this.rApp = rApp;
        currentScope = globals;
    }

    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        //first instantiate components
    }

    /**
     * Lets build the model representation
     * Models consist of properties and schema
     * (1) set app properties to the model
     * (2) set schema fields to the model
     * (3) add model to the app
     * (4) NOTE: we add models to the spaces in the next step, which is done for the ravel app
     *
     * @param ctx
     */
    @Override
    public void enterModelScope(RavelParser.ModelScopeContext ctx) {
        //Create models and fields set properties, add to
        String name = ctx.Identifier().getText();
        String type = ctx.modelType().getText();
        Model m = new Model(name, Model.getType(type));
        LOGGER.log(Level.INFO, "Creating >>{0}<< model {1}", new Object[]{type, name});
        //TODO: not a clean solution
        Scope propScope = ctx.scope.getNestedScope("properties");
        LOGGER.info("Adding models properties: [");
        String propertyDebug = "";
        List<VariableSymbol> prop = (List<VariableSymbol>) propScope.getAllSymbols();
        Iterator<VariableSymbol> p = prop.iterator();
        while (p.hasNext()) {
            VariableSymbol vs = p.next();
            String prope_name = vs.getName();
            String value = vs.getValue();
            propertyDebug += prope_name + ":" + value + ",";
            m.setProperty(prope_name, value);
        }
        LOGGER.info(propertyDebug + "]");

        // create fields for the schema
        Scope schemaScope = ctx.scope.getNestedScope("schema");
        LOGGER.info("Adding model fields to the schema:");
        List<FieldSymbol> schema = (List<FieldSymbol>) schemaScope.getAllSymbols();
        LOGGER.info("Adding model # " + schema.size() + " fields to the schema:");
        Iterator<FieldSymbol> s = schema.iterator();
        while (s.hasNext()) {
            boolean field_has_options = false;
            FieldSymbol fs = s.next();
            RavelParser.FieldDeclarationContext field_ctx = (RavelParser.FieldDeclarationContext) fs.getDefNode();
            //Field type name
            String field_type = field_ctx.field_type().getText();
            //get field type
            Field.Type ft = Field.getType(field_ctx.field_type().start.getType());

            Builder f_concreate = null;
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
            RavelParser.ElementValuePairsContext fieldOpt = field_ctx.elementValuePairs();


            if (fieldOpt != null) {
                field_has_options = true;
                List<RavelParser.ElementValuePairContext> valp;
                valp = fieldOpt.elementValuePair();
                Iterator<RavelParser.ElementValuePairContext> optPair = valp.iterator();
                String args = "";
                while (optPair.hasNext()) {
                    RavelParser.ElementValuePairContext el = optPair.next();
                    //This is assums that field can not have expression
                    //TODO: needs redesign
                    String optName = el.Identifier().getText();
                    String optValue = el.elementValue().getText();
                    args += optName + ":" + optValue + ",";
                    f_concreate.addOption(optName, optValue);
                }
                LOGGER.info("field arguments: [" + args + "]");
            }
            f_concreate.hasOptions(field_has_options);
            m.addField(fs.getName(), f_concreate.build());
            rApp.addModel(m.getName(), m);
        }//end field constructions

    }

    /**
     * Lets build controller representations
     * (1) set all declarations
     * (2) create and add all event to controller
     * (3) add controller to ravel app
     * (4) all references to the spaces is done in next build step
     * @param ctx
     */
    @Override
    public void enterControllerScope(RavelParser.ControllerScopeContext ctx) {
        //create controllers
        String name = ctx.Identifier().getText();
        List<VariableSymbol> cntrVars =  ((ComponentSymbol)ctx.scope).getDefinedFields();
        List<EventSymbol> events = ((ControllerSymbol)ctx.scope).getEvents();
        List<ReferenceSymbol> referenceSymbols = ((ControllerSymbol) ctx.scope).getRefenceSymbols();
        LOGGER.info( name + "controller: " + "#-vars in "
                + cntrVars.size() + " #-events: " + events.size()
                + " #-ref: " + referenceSymbols.size());
        LOGGER.info(ctx.scope.toString());
        Controller ctrl = new Controller(name);
        for (VariableSymbol s: cntrVars) {
            Variable var = makeVariable(s);
            if(s == null){
                throw new IllegalArgumentException("Line: " + ctx.start.getLine()
                        + "Could not create variable" +s.toString());
            }
            LOGGER.info(var.toString());
            ctrl.addVar(s.getName(), var);
        }
        for(ReferenceSymbol r: referenceSymbols){
            //TODO: needs a clever way to create ref objects pointing to the right object
            ctrl.addRef(r.getName(), r.getReference());
        }
        //create events. pew!
        for(EventSymbol e: events){
            String ename = e.getName();
            Event event = new Event(ename);

            ctrl.addEvent(ename, event);
        }


    }
    private Variable makeVariable(VariableSymbol vs) {
        Variable.Builder var  = new Variable.Builder();
        var.name(vs.getName())
                .stringValue(vs.getValue());
        //determine the type of the value
        RavelParser.PropContext node = ((VarAssigmentContext)vs.getDefNode()).prop();
//        prop
//                : Identifier
//                | boolean_r
//                | IntegerLiteral
//                | FloatingPointLiteral
//        ;
        LOGGER.fine(node.getText());
        LOGGER.fine("Parent to string" + node.parent.toString());
        try {
            String value = node.boolean_r().getText();
            var.stringType("boolean");
           return var.value(Boolean.parseBoolean(value)).build();
        } catch (NullPointerException e){ }

        try{
            String value = node.IntegerLiteral().getText();
            var.stringType("integer");
            return var.value(Integer.parseInt(value)).build();
        }catch (NullPointerException e){ }
        try{
            String value = node.FloatingPointLiteral().getText();

            var.stringType("number");
            return var.value(Float.parseFloat(value)).build();
        } catch (NullPointerException e){ }
        try{
            String value = node.Identifier().getText();
            var.stringType("string");
            return  var.value(value).build();
        } catch (NullPointerException e){ }
        //we could build and return here, but we need to be sure that parsing has identified
        //the right value.
        //merge prop and varAssigments
        return null;
    }
    /**
     * TODO: build views
     */

    /**
     * NOTE: Space is build in the next step, because we need to be sure of the order
     * @param ctx
     */
    @Override
    public void enterSpaceScope(RavelParser.SpaceScopeContext ctx) {

    }
}
