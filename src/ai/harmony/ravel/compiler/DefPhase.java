package ai.harmony.ravel.compiler;

import ai.harmony.ravel.RavelApplication;
import ai.harmony.ravel.antlr4.RavelBaseListener;
import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.scope.GlobalScope;
import ai.harmony.ravel.compiler.scope.LocalScope;
import ai.harmony.ravel.compiler.scope.Scope;
import ai.harmony.ravel.compiler.symbol.*;
import ai.harmony.ravel.primitives.InstanceSymbol;
import ai.harmony.ravel.primitives.Model;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by lauril on 8/17/16.
 */
public class DefPhase extends RavelBaseListener {
    RavelApplication ravelApp;
    Scope currentScope;
    //will be imports eventually
    GlobalScope globalScope;
    int intend = 0;
    boolean walked = false;

    public RavelApplication getRavelApp() {
        if (walked) {
            return ravelApp;
        } else {
            return null;
        }
    }



    void prettyPrint(String s){
        System.out.println(getTab() + s);
    }
    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        ravelApp = new RavelApplication();
        GlobalScope g = new GlobalScope(null);
        ctx.scope = g;
        pushScope(g);
    }


    @Override
    public void enterModelScope(RavelParser.ModelScopeContext ctx) {
        String name = ctx.Identifier().getText();
        String type = ctx.modelType().getText();
        ModelSymbol model = new ModelSymbol(name, Model.getType(type));
        ctx.scope = model;
        pushScope(model);
    }

    @Override
    public void enterPropertiesScope(RavelParser.PropertiesScopeContext ctx) {
        LocalScope ls = new LocalScope("properties", currentScope);
        ctx.scope = ls;
        pushScope(ls);
        //can only be defined ONCE per model, through error otherwise
    }


    @Override
    public void exitPropertiesScope(RavelParser.PropertiesScopeContext ctx) {
        popScope();
    }

    @Override
    public void enterSchemaScope(RavelParser.SchemaScopeContext ctx) {
        //can only be defined ONCE per model, through error otherwise
        LocalScope ls = new LocalScope("schema", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }

    @Override
    public void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        // can only be inside the schema scope!
        String name = ctx.Identifier().getText();
        String fType = ctx.field_type().getText();
        FieldSymbol fs = new FieldSymbol(name);
        //TODO: do we need type here?
        currentScope.define(fs);

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
        popScope();
    }

    @Override
    public void enterControllerScope(RavelParser.ControllerScopeContext ctx) {
        String name = ctx.Identifier().getText();
        ControllerSymbol ctr = new ControllerSymbol(name);
        ctx.scope = ctr;
        pushScope(ctr);

    }

    @Override
    public void enterEventScope(RavelParser.EventScopeContext ctx) {
        //define event scope
        //define parameters in the scope
        String name = ctx.qualified_name().getText();
        EventSymbol es = new EventSymbol(name);
        ctx.scope = es;
        pushScope(es);
    }

//    @Override
//    public void enterQueryOperations(RavelParser.QueryOperationsContext ctx) { }
//
//    @Override
//    public void exitQueryOperations(RavelParser.QueryOperationsContext ctx) { }

    @Override
    public void exitEventScope(RavelParser.EventScopeContext ctx) {
        intend--;
    }

    @Override
    public void exitControllerScope(RavelParser.ControllerScopeContext ctx) {
        popScope();

    }

    @Override
    public void enterVarAssigment(RavelParser.VarAssigmentContext ctx) {
        String name = ctx.Identifier().getText();
        VariableSymbol vs = new VariableSymbol(name);
        vs.setScope(currentScope);
        vs.setDefNode(ctx);
        currentScope.define(vs);
    }

    @Override
    public void exitVarAssigment(RavelParser.VarAssigmentContext ctx) {

    }
    @Override
    public void enterReferenceAssigment(RavelParser.ReferenceAssigmentContext ctx) {
        ReferenceSymbol rs = new ReferenceSymbol(ctx.Identifier().getText(), ctx.qualified_name().getText());
        rs.setScope(currentScope);
        rs.setDefNode(ctx);
        currentScope.define(rs);
    }

    @Override
    public void exitReferenceAssigment(RavelParser.ReferenceAssigmentContext ctx) { }

    @Override
    public void enterSpaceScope(RavelParser.SpaceScopeContext ctx) {
        String name = ctx.Identifier().getText();
        SpaceSymbol ssb = new SpaceSymbol(name);
        ctx.scope = ssb;
        pushScope(ssb);
    }

    @Override public void enterPlatformScope(RavelParser.PlatformScopeContext ctx) {
        LocalScope ls = new LocalScope("platform", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }

    @Override public void exitPlatformScope(RavelParser.PlatformScopeContext ctx) {
        popScope();
    }


    @Override public void enterModelInstanciation(RavelParser.ModelInstanciationContext ctx) {
        LocalScope ls = new LocalScope("models", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }

    @Override public void exitModelInstanciation(RavelParser.ModelInstanciationContext ctx) {
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
        popScope();
    }


    @Override public void enterSourceLinks(RavelParser.SourceLinksContext ctx) {
        LocalScope ls = new LocalScope("sources", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }

    @Override public void exitSourceLinks(RavelParser.SourceLinksContext ctx) {
        popScope();
    }


    @Override public void enterInstance(RavelParser.InstanceContext ctx) {
        InstanceSymbol is = new InstanceSymbol(ctx.Identifier().getText());
        is.setScope(currentScope);
        is.setDefNode(ctx);
        currentScope.define(is);
    }

    @Override public void exitInstance(RavelParser.InstanceContext ctx) { }



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

