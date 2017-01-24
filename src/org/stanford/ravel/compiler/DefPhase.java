package org.stanford.ravel.compiler;


import jdk.nashorn.internal.ir.Terminal;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stanford.antlr4.RavelBaseListener;
import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.scope.GlobalScope;
import org.stanford.ravel.compiler.scope.LocalScope;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.symbol.InstanceSymbol;
import org.stanford.ravel.compiler.types.ArrayType;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.Model;
import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.symbol.*;
import org.stanford.ravel.primitives.Primitive;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by lauril on 8/17/16.
 */
public class DefPhase extends RavelBaseListener {
    private static Logger LOGGER = Logger.getLogger(DefPhase.class.getName());
    private Scope currentScope;
    //will be imports eventually

    private GlobalScope globalScope;
    private int intend = 0;
    private boolean walked = false;
    private int nextBlockId = 1;

    public GlobalScope getGlobalScope() {
        assert walked;

        return globalScope;
    }

    private void prettyPrint(String s){
        System.out.println(getTab() + s);
    }

    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        globalScope = new GlobalScope();
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
        //LOGGER.info("ADDING " + type +" Model: " + name);
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
    public void enterSchemaScope(RavelParser.SchemaScopeContext ctx) {
        //can only be defined ONCE per model, through error otherwise
        LocalScope ls = new LocalScope("schema", currentScope);
        ctx.scope = ls;
        currentScope.nest(ls);
        pushScope(ls);
    }

    private static Type typeFromField(String fieldType) {
        switch (fieldType) {
            case "ByteField":
                return new ArrayType(PrimitiveType.BYTE);
            case "BooleanField":
                return PrimitiveType.BOOL;
            case "StringField":
                return PrimitiveType.STR;
            case "IntegerField":
                return PrimitiveType.INT32;
            case "NumberField":
                return PrimitiveType.DOUBLE;
            case "DateField":
                return PrimitiveType.DATE;
            case "DateTimeField":
                return PrimitiveType.DATE_TIME;
            case "TimeStampField":
                return PrimitiveType.TIMESTAMP;
            default:
                return PrimitiveType.ANY;
        }
    }

    @Override
    public void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        // can only be inside the schema scope!
        String name = ctx.Identifier().getText();
        FieldSymbol fs = new FieldSymbol(name);
        fs.setDefNode(ctx);
        fs.setScope(currentScope);

        String fieldType = ctx.field_type().getText();
        fs.setType(typeFromField(fieldType));

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
        List<Scope> modelCheck = currentScope.getNestedScopes();
        //we know there is only two scopes
        if ( modelCheck.size() != 2 ){
            throw new RuntimeException("Expecting two scopes in the model, found: " + modelCheck.size());
        }
        if (! currentScope.hasNestedScope("properties")  ) {
            throw new RuntimeException("Missing declaration of properties in the model!") ;
        }
        if(! currentScope.hasNestedScope("schema") ) {
            throw new RuntimeException("Missing declaration of schema in the model!") ;
        }

        ((ModelSymbol)currentScope).createModelType();

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
        String modelVarName = ctx.Identifier(0).getText();
        String eventName = ctx.Identifier(1).getText();
        //we create a local scope for each event
        EventSymbol es = new EventSymbol(modelVarName + "." + eventName);
        ctx.scope = es;
        es.setDefNode(ctx);
        currentScope.define(es);

        Symbol modelVarSym = currentScope.resolve(modelVarName);

        pushScope(es);

        VariableSymbol selfVar = new VariableSymbol("self");

        if (modelVarSym == null) {
            abortParsing(ctx, "invalid event declaration " + es.getName() + " (undeclared model)");
            return;
        }
        if (!(modelVarSym instanceof VariableSymbol)) {
            abortParsing(ctx, "invalid event declaration " + es.getName() + " (does not refer to a declared model)");
            return;
        }
        Type modelType =  ((VariableSymbol) modelVarSym).getType();
        if (!(modelType instanceof ModelType)) {
            abortParsing(ctx, "invalid event declaration " + es.getName() + " (does not refer to a declared model)");
            return;
        }

        selfVar.setType(((ModelType) modelType).getInstanceType());
        currentScope.define(selfVar);
    }

    @Override
    public void enterIdent_decl(RavelParser.Ident_declContext ctx) {
        String varName = ctx.Identifier().getText();

        Symbol existing = currentScope.resolve(varName);
        if (existing != null && existing instanceof VariableSymbol && ctx.type() == null) {
            // assignment, not declaration, handle it later
            return;
        }

        VariableSymbol var = new VariableSymbol(varName);
        var.setDefNode(ctx);

        Type type;
        if (ctx.type() != null) {
            Symbol typeSymbol = currentScope.resolve(ctx.type().Identifier().getText());
            if (typeSymbol == null || !(typeSymbol instanceof TypeSymbol)) {
                abortParsing(ctx.type(), ctx.type().Identifier().getText() + " does not name a type");
                return;
            }
            type = ((TypeSymbol) typeSymbol).getDefinedType();
        } else {
            type = PrimitiveType.ANY;
        }
        var.setType(type);
        currentScope.define(var);
    }

    @Override
    public void enterTypedIdentDecl(RavelParser.TypedIdentDeclContext ctx) {
        String varName = ctx.Identifier().getText();
        VariableSymbol var = new VariableSymbol(varName);

        Symbol typeSymbol = currentScope.resolve(ctx.type().Identifier().getText());
        if (typeSymbol == null || !(typeSymbol instanceof TypeSymbol)) {
            abortParsing(ctx.type(), ctx.type().Identifier().getText() + " does not name a type");
            return;
        }
        Type type = ((TypeSymbol) typeSymbol).getDefinedType();
        var.setType(type);
        var.setDefNode(ctx);
        currentScope.define(var);
    }

    @Override
    public void enterBlock(RavelParser.BlockContext ctx) {
        LocalScope ls = new LocalScope("block_" + nextBlockId++, currentScope);
        ls.setDefNode(ctx);
        ctx.scope = ls;
        currentScope.nest(ls);
        pushScope(ls);
    }

    @Override
    public void exitBlock(RavelParser.BlockContext ctx) {
        popScope();
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


    @Override public void enterModelInstantiation(RavelParser.ModelInstantiationContext ctx) {
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
    }

    @Override public void exitModelInstantiation(RavelParser.ModelInstantiationContext ctx) {
        for (Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addModels(re.getName(),(InstanceSymbol) re);
        }
        popScope();
    }

    @Override public void enterControllerInstantiation(RavelParser.ControllerInstantiationContext ctx) {
        LocalScope ls = new LocalScope("controllers", currentScope);
        ctx.scope = ls;
        pushScope(ls);
    }

    @Override public void exitControllerInstantiation(RavelParser.ControllerInstantiationContext ctx) {
        for (Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addControllers(re.getName(),(InstanceSymbol) re);
        }
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

