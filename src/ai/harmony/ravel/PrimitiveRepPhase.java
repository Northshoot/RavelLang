package ai.harmony.ravel;

import ai.harmony.antlr4.RavelBaseListener;
import ai.harmony.antlr4.RavelParser;
import ai.harmony.antlr4.RavelParser.VarAssignmentContext;
import ai.harmony.ravel.compiler.scope.GlobalScope;
import ai.harmony.ravel.compiler.scope.LocalScope;
import ai.harmony.ravel.compiler.scope.Scope;
import ai.harmony.ravel.compiler.symbol.*;
import ai.harmony.ravel.primitives.*;
import ai.harmony.ravel.primitives.Fields.*;
import ai.harmony.ravel.primitives.Fields.Field.Builder;
import sun.jvm.hotspot.oops.Instance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by lauril on 8/30/16.
 */
public class PrimitiveRepPhase extends RavelBaseListener {
    private static Logger LOGGER = Logger.getLogger(PrimitiveRepPhase.class.getName());
    private GlobalScope globals;
    private Scope currentScope;
    private RavelApplication rApp;

    public PrimitiveRepPhase(GlobalScope globals, RavelApplication rApp) {
        this.globals = globals;
        this.rApp = rApp;
        currentScope = globals;
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
        List<RavelParser.ParamContext> component_parametersContext = ctx.component_parameters().params().param();
        for(RavelParser.ParamContext t: component_parametersContext){
            m.addParam(t.getText());
        }
        Scope propScope = ctx.scope.getNestedScope("properties");
        LOGGER.info("Adding models properties: [");
        String propertyDebug = "";
        List<VariableSymbol> prop = (List<VariableSymbol>) propScope.getAllSymbols();
        for (VariableSymbol s: prop) {
            Variable var = makeVariable(s);
            if(var == null){
                throw new IllegalArgumentException("Line: " + s.getDefNode().start.getLine()
                        + " Could not create variable " +s.toString());
            }
            m.setProperty(var);
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
        String name = ctx.Identifier().getText(); //
        Controller ctrl = new Controller(name);
        //get controller args
        List<RavelParser.ParamContext> component_parametersContext = ctx.component_parameters().params().param();
        for(RavelParser.ParamContext t: component_parametersContext){
            ctrl.addParam(t.getText());
        }
        //get all variables
        List<VariableSymbol> cntrVars =  ((ComponentSymbol)ctx.scope).getDefinedFields();
        //get all events
        List<EventSymbol> events = ((ControllerSymbol)ctx.scope).getEvents();
        List<ReferenceSymbol> referenceSymbols = ((ControllerSymbol) ctx.scope).getRefenceSymbols();
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
            String ref = r.getValue();
            String[] ref_array = ref.split("\\.");
            if(ctrl.hasParam(ref_array[0])) {
                ctrl.addRef(r.getName(), r.getValue());
            } else {
                throw new IllegalArgumentException("Line: " + r.getDefNode().start.getLine()
                        +" Could not find parameter for var " + r.getName() + " with reference "
                        + ref + "\nAlternatives: " + ctrl.getParamsNames());
            }
        }
        //create events. pew!
        for(EventSymbol es: events){
            Event e = makeEvent(es);
            ctrl.addEvent(e.getName(), e);
        }

        rApp.addController(name, ctrl);
    }

    private Event makeEvent(EventSymbol e) {
        //TODO: this is simplified version of the event
        // no if, else, for are handled for now

        Event.Builder event = new Event.Builder();
        event.name(e.getName());
        RavelParser.EventScopeContext ectx = (RavelParser.EventScopeContext) e.getDefNode();
        //currently only context is passed
        RavelParser.FunctionArgsListContext args  =  ectx.function_args().functionArgsList();
        List<RavelParser.FunctionArgContext> functionArgContexts = args.functionArg();
        Iterator<RavelParser.FunctionArgContext> arg = functionArgContexts.iterator();
        while(arg.hasNext()){
            RavelParser.FunctionArgContext argContext = arg.next();
            //TODO: doubtfully the best way, fix when time (HA!)
            event.addArg(argContext.Identifier(0).getText(), argContext.Identifier(1).getText());
        }
        //get all variables
        List<VariableSymbol> eventVars =  ((EventSymbol)ectx.scope).getDefinedFields();
        for (VariableSymbol s: eventVars) {
            Variable var = makeVariable(s);
            if(s == null){
                throw new IllegalArgumentException("Line: " + ectx.start.getLine()
                        + "Could not create variable" +s.toString());
            }
            event.addVariable(var);
        }
        LOGGER.info(event.build().toString());
        return event.build();
    }

    private Variable makeVariable(VariableSymbol vs) {
        Variable.Builder var = new Variable.Builder();
        var.name(vs.getName());
        RavelParser.PropValueContext propValueContext = ((VarAssignmentContext) vs.getDefNode()).propValue();

//        prop
//                : Identifier
//                | boolean_r
//                | IntegerLiteral
//                | FloatingPointLiteral
//
        //get class of next child, we know it only can be one
        if (propValueContext.getChild(0) instanceof RavelParser.PropContext) {
            RavelParser.PropContext node = propValueContext.prop();
            try {
                String value = node.boolean_r().getText();
                var.stringType("boolean");
                return var.value(Boolean.parseBoolean(value)).build();
            } catch (NullPointerException e) { }
            try {
                String value = node.IntegerLiteral().getText();
                var.stringType("integer");
                return var.value(Integer.parseInt(value)).build();
            } catch (NullPointerException e) { }
            try {
                String value = node.FloatingPointLiteral().getText();

                var.stringType("number");
                return var.value(Float.parseFloat(value)).build();
            } catch (NullPointerException e) { }
            try {
                String value = node.StringLiteral().getText();
                var.stringType("string");
                return var.value(value).build();
            } catch (NullPointerException e) { }
            try {
                String value = node.Identifier().getText();
                var.stringType("assignment");
                return var.value(value).build();
            } catch (NullPointerException e ) {}
            //we could build and return here, but we need to be sure that parsing has identified
            //the right value.
            //merge prop and varAssignments
        } else if (propValueContext.getChild(0) instanceof RavelParser.PropArrayContext){
            //either one has to be not null
            RavelParser.PropArrayContext propArrayContext = propValueContext.propArray();
            List<RavelParser.PropContext> prop = propArrayContext.prop();
            var.stringType("array");
            List<String> valList = new ArrayList<>();
            for(RavelParser.PropContext p: prop) valList.add(p.getText());
            return var.value(valList).build();
        } else {
            throw new RuntimeException("Illegal child when creating variable, expecting property or property array, got: " +
                    ((VarAssignmentContext) vs.getDefNode()).propValue().getChild(0).getClass().getName());
        }
        return null;
    }

    @Override
    public void enterSpaceScope(RavelParser.SpaceScopeContext ctx){
        LOGGER.info("******* Entering Space scope *******");
        String name = ctx.Identifier().getText();
        SpaceSymbol ssb = (SpaceSymbol)ctx.scope;
        Space space  = new Space(ssb.getName());
        //TODO: makes this static part of the process rather than hardcoded strings

        /** build platform */
        Map<String, ReferenceSymbol> prop = ssb.getPlatform();
        Platform.Builder p = new Platform.Builder();
        p.name(prop.get("language").getName());
        p.template(prop.get("templates").getValue());
        p.system(prop.get("system").getValue());
        /** build sinks */
        Map<String, ReferenceSymbol> sinks = ssb.getSink();
        for(ReferenceSymbol re: sinks.values()){
            //TODO: is reference starting good?
            String identifier = re.getName();
            String reference = re.getValue();
            //must start with platform.system.
            if(reference.startsWith("platform.system.")){
                p.sink(identifier, reference);
            }
        }
        /** build sources */
        Map<String, ReferenceSymbol> source = ssb.getSource();

        for(ReferenceSymbol re: source.values()){
            //TODO: is reference starting good?
            String identifier = re.getName();
            String reference = re.getValue();
            //must start with platform.system.
            if(reference.startsWith("platform.system.")){
                p.source(identifier, reference);
            }
        }
        space.add(p.build());

        /** build models */
        Map<String, InstanceSymbol> modelInst = ssb.getModels();

        //add model and set all the parameters to the parameter map
        for(String mName: modelInst.keySet()){
            //get the instance symbol
            InstanceSymbol is = modelInst.get(mName);
            //get the model
            Model m = rApp.getModel(is.getInstanceName());
            //set parameters
            Map<String, String> ismap = is.getParameterMap();
            for(Map.Entry<String, String> entry : ismap.entrySet()) {
                m.setParam(entry.getKey(), entry.getValue());
            }
        }

        /** build controllers */
        Map<String, InstanceSymbol> ctrInst = ssb.getControllers();




    }

    /**
     * TODO: build views
     */
}
