package org.stanford.ravel.compiler;



import org.stanford.antlr4.RavelBaseListener;
import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.ir.Registers;
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
import org.stanford.ravel.primitives.ModelEvent;

import java.util.List;

/**
 * Created by lauril on 8/17/16.
 */
public class DefPhase extends RavelBaseListener {
    private final boolean debug;

    private final RavelCompiler driver;

    public DefPhase(RavelCompiler driver, boolean debug) {
        this.driver = driver;
        this.debug = debug;
    }

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
        globalScope.setDefNode(ctx);
        pushScope(globalScope);
    }

    @Override
    public void enterModelScope(RavelParser.ModelScopeContext ctx) {
        String name = ctx.Identifier().getText();
        String type = ctx.modelType().getText();
        ModelSymbol model = new ModelSymbol(name, Model.getType(type));
        currentScope.define(model);
        ctx.scope=model;
        model.setDefNode(ctx);
        pushScope(model);
        //LOGGER.info("ADDING " + type +" Model: " + name);
    }

    @Override
    public void enterPropertiesScope(RavelParser.PropertiesScopeContext ctx) {
        LocalScope ls = new LocalScope("properties", currentScope);
        ctx.scope = ls;
        currentScope.nest(ls);
        ls.setDefNode(ctx);
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
        // FIXME figure out the right type
        vs.setType(PrimitiveType.ANY);
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
        ls.setDefNode(ctx);
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
        ctr.setDefNode(ctx);
        currentScope.define(ctr);
        pushScope(ctr);
    }

    @Override
    public void enterEventScope(RavelParser.EventScopeContext ctx) {
        //define event scope
        //define parameters in the scope
        String modelVarName = ctx.Identifier(0).getText();
        String eventName = ctx.Identifier(1).getText();

        EventSymbol es;
        try {
            ModelEvent event = ModelEvent.valueOf(eventName);
            es = new EventSymbol(modelVarName, event);
        } catch(IllegalArgumentException e) {
            emitError(ctx, "invalid event declaration " + modelVarName + "." + eventName + ": not a valid event name");
            return;
        }

        //we create a local scope for each event
        ctx.scope = es;
        es.setDefNode(ctx);
        currentScope.define(es);

        Symbol modelVarSym = currentScope.resolve(modelVarName);

        pushScope(es);

        VariableSymbol selfVar = new VariableSymbol("self");
        // it's a stretch to say that the whole event defines self, but if
        // we ever need to emit type errors related to it, that's probably the
        // best AST node to attach to it
        selfVar.setDefNode(ctx);
        selfVar.setRegister(Registers.SELF_REG);

        if (modelVarSym == null) {
            emitError(ctx, "invalid event declaration " + es.getName() + ": undeclared model");
            return;
        }
        if (!(modelVarSym instanceof VariableSymbol)) {
            emitError(ctx, "invalid event declaration " + es.getName() + ": does not refer to a declared model");
            return;
        }
        Type modelType =  ((VariableSymbol) modelVarSym).getType();
        if (!(modelType instanceof ModelType)) {
            emitError(ctx, "invalid event declaration " + es.getName() + ": does not refer to a declared model");
            return;
        }

        selfVar.setType(((ModelType) modelType).getContextType());
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
                emitError(ctx.type(), ctx.type().Identifier().getText() + " does not name a type");
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
            emitError(ctx.type(), ctx.type().Identifier().getText() + " does not name a type");
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
        ssb.setDefNode(ctx);
        currentScope.define(ssb);
        pushScope(ssb);
    }

    @Override public void enterPlatformScope(RavelParser.PlatformScopeContext ctx) {
        LocalScope ls = new LocalScope("platform", currentScope);
        ctx.scope = ls;
        ls.setDefNode(ctx);
        currentScope.nest(ls);
        pushScope(ls);
    }

    @Override public void exitPlatformScope(RavelParser.PlatformScopeContext ctx) {

        for (Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addPlatform(re.getName(),(ReferenceSymbol) re);
        }

        popScope();
    }

    @Override
    public void enterRef_assign(RavelParser.Ref_assignContext ctx) {
        assert currentScope.getEnclosingScope() instanceof SpaceSymbol;

        String currentScopeName = currentScope.getName();

        // FIXME this can actually never be true, because models and controllers
        // use instantiation (InstanceSymbol) not ref_assign (ReferenceSymbol/ConstantSymbol)
        boolean allowLiteral = currentScopeName.equals("models") || currentScopeName.equals("controllers");

        String name = ctx.qualified_name().getText();
        RavelParser.Simple_expressionContext value = ctx.simple_expression();
        if (value.literal() != null) {
            if (allowLiteral) {
                Object literal = ParserUtils.literalToValue(value.literal());
                ConstantSymbol sym = new ConstantSymbol(name, literal);
                sym.setDefNode(ctx);
                sym.setType(ParserUtils.typeFromLiteral(literal));
                currentScope.define(sym);
            } else {
                emitError(value.literal(), "literal value not allowed in this context");
            }
        } else {
            ReferenceSymbol ref = new ReferenceSymbol(name, value.qualified_name().getText());
            ref.setDefNode(ctx);
            currentScope.define(ref);
        }
    }

    @Override public void enterModelInstantiation(RavelParser.ModelInstantiationContext ctx) {
        LocalScope ls = new LocalScope("models", currentScope);
        ctx.scope = ls;
        ls.setDefNode(ctx);
        currentScope.nest(ls);
        pushScope(ls);
    }
    @Override public void enterInstance(RavelParser.InstanceContext ctx) {
        String instanceName = ctx.instance_name().getText();
        String varName = ctx.Identifier().getText();
        Symbol referred = currentScope.resolve(instanceName);
        if (referred == null || !(referred instanceof ComponentSymbol)) {
            emitError(ctx, "instantiation of " + varName + " refers to non-existing component " + instanceName);
            return;
        }
        if (currentScope.getName().equals("models")) {
            if (!(referred instanceof ModelSymbol)) {
                emitError(ctx, "instantiation of model " + varName + " must refer to a model, not a " + referred.getClass().getName());
                return;
            }
        } else if (currentScope.getName().equals("controllers")) {
            if (!(referred instanceof ControllerSymbol)) {
                emitError(ctx, "instantiation of controller " + varName + " must refer to a model, not a " + referred.getClass().getName());
                return;
            }
        }

        InstanceSymbol is = new InstanceSymbol(varName, (ComponentSymbol)referred);
        ctx.symbol = is;
        is.setDefNode(ctx);
        is.setType(((ComponentSymbol) referred).getDefinedType());
        currentScope.define(is);
    }

    @Override public void enterParameterAssignments(RavelParser.ParameterAssignmentsContext ctx) {
        RavelParser.InstanceContext instance = (RavelParser.InstanceContext) ctx.getParent();

        // if instance.symbol == null, enterInstance returned early before creating the symbol,
        // which indicates a parse/type error
        if (instance.symbol == null)
            return;
        for (RavelParser.Param_assigContext p: ctx.param_assig()) {
            RavelParser.Simple_expressionContext pval = p.param_val().simple_expression();

            String pname = p.Identifier().getText();
            if (pval.qualified_name() != null) {
                Symbol referencedSymbol = currentScope.resolve(pval.qualified_name().getText());
                if (referencedSymbol instanceof ConstantSymbol)
                    instance.symbol.addParameter(pname, ((ConstantSymbol) referencedSymbol).getValue());
                else if (referencedSymbol instanceof InstanceSymbol)
                    instance.symbol.addParameter(pname, referencedSymbol);
                else // null, or some weird symbol (eg another SpaceSymbol or a ModelSymbol)
                    emitError(ctx, "invalid parameter value (not a variable in scope)");
            } else {
                instance.symbol.addParameter(pname, ParserUtils.literalToValue(pval.literal()));
            }
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
        ls.setDefNode(ctx);
        currentScope.nest(ls);
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
        ls.setDefNode(ctx);
        currentScope.nest(ls);
        pushScope(ls);
    }
    @Override public void exitSinkLinks(RavelParser.SinkLinksContext ctx) {
        for (Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addSink(re.getName(),(ReferenceSymbol) re);
        }
        popScope();
    }


    @Override public void enterSourceLinks(RavelParser.SourceLinksContext ctx) {
        LocalScope ls = new LocalScope("sources", currentScope);
        ctx.scope = ls;
        ls.setDefNode(ctx);
        currentScope.nest(ls);
        pushScope(ls);
    }

    @Override public void exitSourceLinks(RavelParser.SourceLinksContext ctx) {
        for (Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addSource(re.getName(),(ReferenceSymbol) re);
        }
        popScope();
    }
    @Override public void exitSpaceScope(RavelParser.SpaceScopeContext ctx) {
        popScope();
    }

    /**
     * The end of the file and the end of the App
     * TODO: handle multiple files and imports
     * @param ctx
     */
    @Override
    public void exitFile_input(RavelParser.File_inputContext ctx) {
        walked = true;
    }

    private void pushScope(Scope s) {
        currentScope = s;
        if (debug)
            prettyPrint("entering: "+currentScope.getName()+":"+s);
        intend++;
    }

    private void popScope() {
        if (debug)
            prettyPrint("leaving: "+currentScope.getName()+":"+currentScope);
        currentScope = currentScope.getEnclosingScope();
        intend--;
    }

    private void emitError(ParserRuleContext rctx, String reason){
        driver.emitError(new SourceLocation(rctx), reason);
    }

    private String getTab(){
        String tab="";
        for(int i=0; i<intend; i++){
            tab+="\t";
        }
        return tab;
    }
}

