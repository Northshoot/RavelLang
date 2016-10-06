package ai.harmony.ravel.compiler;


import ai.harmony.antlr4.RavelBaseListener;
import ai.harmony.antlr4.RavelParser;
import ai.harmony.ravel.compiler.scope.GlobalScope;
import ai.harmony.ravel.compiler.scope.LocalScope;
import ai.harmony.ravel.compiler.scope.Scope;
import ai.harmony.ravel.compiler.symbol.*;
import ai.harmony.ravel.compiler.symbol.InstanceSymbol;
import ai.harmony.ravel.primitives.Model;
import ai.harmony.ravel.primitives.Space;
import javafx.util.Pair;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by lauril on 8/17/16.
 */
public class DefPhase extends RavelBaseListener {
    private static Logger LOGGER = Logger.getLogger(DefPhase.class.getName());
    Scope currentScope;
    //will be imports eventually
    public GlobalScope globalScope;
    int intend = 0;
    boolean walked = false;



    void prettyPrint(String s){
        //System.out.println(getTab() + s);
    }
    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        globalScope = new GlobalScope(null);
        ctx.scope = globalScope;
        pushScope(globalScope);
    }


    @Override
    public void enterModelScope(RavelParser.ModelScopeContext ctx) {
        String name = ctx.Identifier().getText();
        String type = ctx.modelType().getText();
        ModelSymbol model = new ModelSymbol(name, Model.getType(type));
        currentScope.define(model);
        ctx.scope=model;
        pushScope(model);
        LOGGER.info("ADDING " + type +" Model: " + name);
    }

    @Override
    public void enterPropertiesScope(RavelParser.PropertiesScopeContext ctx) {
        LocalScope ls = new LocalScope("properties", currentScope);
        ctx.scope = ls;
        currentScope.nest(ls);
        pushScope(ls);
        //can only be defined ONCE per model, through error otherwise
    }

    @Override
    public void exitParameterAssignments(RavelParser.ParameterAssignmentsContext ctx) {

    }

    @Override
    public void exitPropertiesScope(RavelParser.PropertiesScopeContext ctx) {
        if(currentScope instanceof SpaceSymbol) {
            for(Symbol re: ctx.scope.getSymbols()) {
                ((SpaceSymbol) currentScope.getEnclosingScope()).addPlatform(re.getName(),(ReferenceSymbol) re);
            }
        }
        popScope();
    }

    @Override
    public void enterSchemaScope(RavelParser.SchemaScopeContext ctx) {
        //can only be defined ONCE per model, through error otherwise
        LocalScope ls = new LocalScope("schema", currentScope);
        ctx.scope = ls;
        currentScope.nest(ls);
        pushScope(ls);
    }

    @Override
    public void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        // can only be inside the schema scope!
        String name = ctx.Identifier().getText();
        FieldSymbol fs = new FieldSymbol(name);
        fs.setDefNode(ctx);
        fs.setScope(currentScope);
        //TODO: do we need type here?
        currentScope.define(fs);
        //LOGGER.info("ADDING FIELD: " +fs.toString());

     }

    @Override
    public void exitFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void exitSchemaScope(RavelParser.SchemaScopeContext ctx) {
        if(currentScope.getName().equals("schema")) {
            popScope();
        } else {
            throw new RuntimeException("Exiting Schema scope while in " + currentScope.getName());
        }
    }

    @Override
    public void exitModelScope(RavelParser.ModelScopeContext ctx) {
        List<Scope> modelCeck = currentScope.getNestedScopes();
        //we know there is only two scopes
        if ( modelCeck.size() != 2 ){
            throw new RuntimeException("Expecting two scopes in the model, found: " + modelCeck.size());
        }
        if (! currentScope.hasNestedScope("properties")  ) {
            throw new RuntimeException("Missing declaration of properties in the model!") ;
        }
        if(! currentScope.hasNestedScope("schema") ) {
            throw new RuntimeException("Missing declaration of schema in the model!") ;
        }

        popScope();
    }



    @Override
    public void enterControllerScope(RavelParser.ControllerScopeContext ctx) {
        String name = ctx.Identifier().getText();
        ControllerSymbol ctr = new ControllerSymbol(name);
        ctx.scope = ctr;
        currentScope.define(ctr);
        pushScope(ctr);

    }

    @Override
    public void enterEventScope(RavelParser.EventScopeContext ctx) {
        //define event scope
        //define parameters in the scope
        String name = ctx.qualified_name().getText();
        //we create a local scope for each event
        EventSymbol es = new EventSymbol(name);
        ctx.scope = es;
        es.setDefNode(ctx);
        currentScope.define(es);
        pushScope(es);

    }

    @Override
    public void exitEventScope(RavelParser.EventScopeContext ctx) {
        popScope();
    }

    @Override
    public void exitControllerScope(RavelParser.ControllerScopeContext ctx) {
        popScope();
    }

    @Override
    public void enterVarAssignment(RavelParser.VarAssignmentContext ctx) {
        intend++;
        String name = ctx.Identifier().getText();
        VariableSymbol vs = new VariableSymbol(name);
        vs.setScope(currentScope);
        vs.setDefNode(ctx);
        currentScope.define(vs);
    }

    @Override
    public void exitVarAssignment(RavelParser.VarAssignmentContext ctx) {
        intend--;
    }

    @Override
    public void enterReferenceAssignment(RavelParser.ReferenceAssignmentContext ctx) {
        intend++;
        ReferenceSymbol rs = new ReferenceSymbol(ctx.key().getText(), ctx.value().getText());
        rs.setScope(currentScope);
        rs.setDefNode(ctx);
        currentScope.define(rs);
    }

    @Override
    public void exitReferenceAssignment(RavelParser.ReferenceAssignmentContext ctx) { intend--;}

    @Override
    public void enterSpaceScope(RavelParser.SpaceScopeContext ctx) {
        String name = ctx.Identifier().getText();
        SpaceSymbol ssb = new SpaceSymbol(name);
        ctx.scope = ssb;
        currentScope.define(ssb);
        pushScope(ssb);
    }

    @Override public void enterPlatformScope(RavelParser.PlatformScopeContext ctx) {
        LocalScope ls = new LocalScope("platform", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }

    @Override public void exitPlatformScope(RavelParser.PlatformScopeContext ctx) {

        for(Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addPlatform(re.getName(),(ReferenceSymbol) re);
        }

        popScope();
    }


    @Override public void enterModelInstanciation(RavelParser.ModelInstanciationContext ctx) {
        LocalScope ls = new LocalScope("models", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }
    @Override public void enterInstance(RavelParser.InstanceContext ctx) {
        InstanceSymbol is = new InstanceSymbol(ctx.Identifier().getText(), ctx);
        ctx.symbol = is;
        is.setScope(currentScope);
        is.setDefNode(ctx);
        currentScope.define(is);

    }



    @Override public void enterParameterAssignments(RavelParser.ParameterAssignmentsContext ctx) {
        RavelParser.InstanceContext instance = (RavelParser.InstanceContext) ctx.getParent();
        List<RavelParser.Param_assigContext> paramAssigContexts = ctx.param_assig();
        for(RavelParser.Param_assigContext p: ctx.param_assig()) {
            ((InstanceSymbol) instance.symbol).addParameter(p.Identifier().getText(), p.param_val().getText());
        }
    }

    @Override public void exitInstance(RavelParser.InstanceContext ctx) {
        if(currentScope.getEnclosingScope() instanceof SpaceSymbol){
            SpaceSymbol ss = (SpaceSymbol) currentScope.getEnclosingScope();
            String cn = currentScope.getName();
           if(cn == "model"){
               ss.addModels(ctx.Identifier().getText(), (InstanceSymbol)ctx.symbol);
           } else if (cn == "controllers") {
               ss.addControllers(ctx.Identifier().getText(), (InstanceSymbol)ctx.symbol);
           } else {
               LOGGER.severe("Not applicable here exitInstance with name: " + cn);
           }
        } else {
            LOGGER.severe("Should be only in space!");
        }

    }
    @Override public void exitModelInstanciation(RavelParser.ModelInstanciationContext ctx) {
        for(Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addModels(re.getName(),(InstanceSymbol) re);
        }
        popScope();
    }

    @Override public void enterControllerInstanciation(RavelParser.ControllerInstanciationContext ctx) {
        LocalScope ls = new LocalScope("controllers", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }

    @Override public void exitControllerInstanciation(RavelParser.ControllerInstanciationContext ctx) {
        popScope();
    }

    @Override public void enterSinkLinks(RavelParser.SinkLinksContext ctx) {
        LocalScope ls = new LocalScope("sinks", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }
    @Override public void exitSinkLinks(RavelParser.SinkLinksContext ctx) {

        for(Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addSink(re.getName(),(ReferenceSymbol) re);
        }
        popScope();
    }


    @Override public void enterSourceLinks(RavelParser.SourceLinksContext ctx) {
        LocalScope ls = new LocalScope("sources", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }

    @Override public void exitSourceLinks(RavelParser.SourceLinksContext ctx) {

        for(Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addSource(re.getName(),(ReferenceSymbol) re);
        }
        popScope();
    }
    @Override public void exitSpaceScope(RavelParser.SpaceScopeContext ctx) {
        popScope();
    }



    /**
     * The end of the file and the end of the app
     * TODO: handle multiple files and imports
     * @param ctx
     */
    @Override
    public void exitFile_input(RavelParser.File_inputContext ctx) {
        walked = true;

    }
    private void pushScope(Scope s) {
        currentScope = s;
        prettyPrint("entering: "+currentScope.getName()+":"+s);
        intend++;
    }

    private void popScope() {
        prettyPrint("leaving: "+currentScope.getName()+":"+currentScope);
        currentScope = currentScope.getEnclosingScope();
        intend--;
    }

    void abortParsing(ParserRuleContext rctx, String reason){
        String exception = reason;
        exception+=" found on line " +String.valueOf(rctx.start.getLine());
        throw new RuntimeException(exception);
    }
    private String getTab(){
        String tab="";
        for(int i=0; i<intend; i++){
            tab+="\t";
        }
        return tab;
    }
}

