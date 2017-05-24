package edu.stanford.ravel.compiler;

import edu.stanford.antlr4.RavelParser;
import edu.stanford.ravel.compiler.symbol.*;
import edu.stanford.ravel.compiler.types.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import edu.stanford.antlr4.RavelBaseListener;
import edu.stanford.ravel.RavelCompiler;
import edu.stanford.ravel.compiler.ir.Registers;
import edu.stanford.ravel.compiler.scope.GlobalScope;
import edu.stanford.ravel.compiler.scope.LocalScope;
import edu.stanford.ravel.compiler.scope.Scope;
import edu.stanford.ravel.primitives.Model;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lauril on 8/17/16.
 */
public class DefPhase extends RavelBaseListener {
    private final boolean debug;

    private GlobalScope globalScope;
    private final RavelCompiler driver;

    public DefPhase(RavelCompiler driver, boolean debug) {
        this.driver = driver;
        this.debug = debug;
    }

    private DefPhase(RavelCompiler driver, GlobalScope globalScope, boolean debug) {
        this.globalScope = globalScope;
        this.driver = driver;
        this.debug = debug;
    }
    private Scope currentScope;
    //TODO: will be imports eventually


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

    private void recursiveDefPhase(String path) {
        try {
            ParseTree tree = driver.treeFromInput(path);
            DefPhase listener = new DefPhase(driver, globalScope, false);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, tree);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void enterImportFrom(RavelParser.ImportFromContext ctx) {
        ImportSymbol is;
        RavelParser.Dotted_nameContext impf = ctx.import_from().dotted_name();


        List<RavelParser.Import_as_nameContext> imp = ctx.import_from().import_as_names().import_as_name();
        for (RavelParser.Import_as_nameContext dname:imp) {
            is = new ImportSymbol(impf.getText()+"." + dname.getText());
            is.setFrom(impf.getText());
            is.setName(dname.getText());
            recursiveDefPhase(is.getPath());
        }

    }

    @Override public void enterImportName(RavelParser.ImportNameContext ctx) {
        ImportSymbol is;
        List<RavelParser.Dotted_as_nameContext> impf = ctx.import_name().dotted_as_names().dotted_as_name();
        for (RavelParser.Dotted_as_nameContext dname:impf) {
            is = new ImportSymbol(dname.getText());
            is.setName(driver.getAppPath() + "/" + dname.getText());
            recursiveDefPhase(is.getPath());
        }

    }

    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        if(this.globalScope == null){
            //we enter first file

            globalScope = new GlobalScope();
        }
        ctx.scope = globalScope;
        globalScope.setDefNode(ctx);
        pushScope(globalScope);
    }

    @Override
    public void enterModelScope(RavelParser.ModelScopeContext ctx) {
        String name = ctx.Identifier().getText();
        String type = ctx.modelType().getText();

        Model.Type modelType;
        try {
            modelType = Model.Type.valueOf(type.toUpperCase());
        } catch(IllegalArgumentException e) {
            emitError(ctx.modelType(), "invalid model type");
            modelType = Model.Type.INVALID;
        }

        ModelSymbol model = new ModelSymbol(name, modelType);
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
    public void enterDirectedFlow(RavelParser.DirectedFlowContext ctx) {
        FlowSymbol fs = new FlowSymbol(true);
        fs.setDefNode(ctx);

        for (TerminalNode ident : ctx.Identifier()) {
            fs.addSpace(ident.getText());
        }

        currentScope.define(fs);
    }

    @Override
    public void enterUndirectedFlow(RavelParser.UndirectedFlowContext ctx) {
        FlowSymbol fs = new FlowSymbol(false);
        fs.setDefNode(ctx);

        for (TerminalNode ident : ctx.Identifier()) {
            fs.addSpace(ident.getText());
        }

        currentScope.define(fs);
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
        ls.setDefNode(ctx);
        pushScope(ls);
    }

    private Type parseType(RavelParser.TypeContext ctx) {
        List<TerminalNode> identList = ctx.Identifier();
        String typeName = identList.get(0).getText();
        Symbol typeSymbol = currentScope.resolve(typeName);
        if (!(typeSymbol instanceof TypeSymbol)) {
            emitError(ctx, typeName + " does not name a type");
            return PrimitiveType.ERROR;
        } else {
            Type baseType = ((TypeSymbol) typeSymbol).getDefinedType();

            Iterator<TerminalNode> it = identList.iterator();
            it.next();
            while (it.hasNext()) {
                String subTypeName = it.next().getText();
                Type subType = baseType.getNestedType(subTypeName);
                typeName = typeName + '.' + subTypeName;
                if (subType == null) {
                    emitError(ctx, typeName + " does not name a type");
                    return PrimitiveType.ERROR;
                }
                baseType = subType;
            }

            for (RavelParser.Array_markerContext array : ctx.array_marker()) {
                if (array.DECIMAL_INTEGER() != null) {
                    baseType = new ArrayType(baseType, Integer.valueOf(array.DECIMAL_INTEGER().getText()));
                } else {
                    baseType = new ArrayType(baseType);
                }
            }

            return baseType;
        }
    }

    @Override
    public void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        // can only be inside the schema scope!
        String name = ctx.Identifier().getText();
        FieldSymbol fs = new FieldSymbol(name);
        fs.setDefNode(ctx);
        fs.setScope(currentScope);

        fs.setType(parseType(ctx.type()));
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
    public void enterInterfaceScope(RavelParser.InterfaceScopeContext ctx) {
        String name = ctx.Identifier().getText();
        InterfaceSymbol sym = new InterfaceSymbol(name);
        ctx.scope = sym;
        sym.setDefNode(ctx);
        currentScope.define(sym);
        pushScope(sym);
    }

    @Override
    public void enterImplementationScope(RavelParser.ImplementationScopeContext ctx) {
        LocalScope ls = new LocalScope("implementation", currentScope);
        ctx.scope = ls;
        currentScope.nest(ls);
        ls.setDefNode(ctx);
        pushScope(ls);
    }

    @Override
    public void exitImplementationScope(RavelParser.ImplementationScopeContext ctx) {
        popScope();
    }

    @Override
    public void enterConfigurationScope(RavelParser.ConfigurationScopeContext ctx) {
        LocalScope ls = new LocalScope("configuration", currentScope);
        ctx.scope = ls;
        currentScope.nest(ls);
        ls.setDefNode(ctx);
        pushScope(ls);
    }

    @Override
    public void exitConfigurationScope(RavelParser.ConfigurationScopeContext ctx) {
        popScope();
    }

    @Override
    public void enterUsesScope(RavelParser.UsesScopeContext ctx) {
        LocalScope ls = new LocalScope("uses", currentScope);
        ctx.scope = ls;
        currentScope.nest(ls);
        ls.setDefNode(ctx);
        pushScope(ls);
    }

    @Override
    public void exitUsesScope(RavelParser.UsesScopeContext ctx) {
        popScope();
    }

    @Override
    public void enterInterfaceDef(RavelParser.InterfaceDefContext ctx) {
        String name = ctx.Identifier().getText();
        Type returnType;

        if (ctx.type() != null) {
            returnType = parseType(ctx.type());
        } else {
            returnType = PrimitiveType.VOID;
        }
        if (returnType instanceof ClassType)
            returnType = ((ClassType) returnType).getInstanceType();

        MethodDeclarationSymbol sym = new MethodDeclarationSymbol(name, returnType, false);
        ctx.symbol = sym;
        sym.setDefNode(ctx);
        currentScope.define(sym);
        pushScope(sym);
    }

    @Override
    public void exitInterfaceDef(RavelParser.InterfaceDefContext ctx) {
        popScope();
    }

    @Override
    public void enterInterfaceEvent(RavelParser.InterfaceEventContext ctx) {
        String name = ctx.Identifier().getText();

        MethodDeclarationSymbol sym = new MethodDeclarationSymbol(name, PrimitiveType.VOID, true);
        ctx.symbol = sym;
        sym.setDefNode(ctx);
        currentScope.define(sym);
        pushScope(sym);
    }

    @Override
    public void exitInterfaceEvent(RavelParser.InterfaceEventContext ctx) {
        popScope();
    }

    @Override
    public void exitInterfaceScope(RavelParser.InterfaceScopeContext ctx) {
        if (((InterfaceSymbol)currentScope).getImplementationScope() == null)
            emitError(ctx, "must define an 'implementation:' block for an interface pointing to the template files");

        ((InterfaceSymbol)currentScope).createInterfaceType();
        popScope();
    }

    @Override
    public void enterViewScope(RavelParser.ViewScopeContext ctx) {
        String name = ctx.Identifier().getText();
        ViewSymbol sym = new ViewSymbol(name);
        ctx.scope = sym;
        sym.setDefNode(ctx);
        currentScope.define(sym);
        pushScope(sym);
    }

    @Override
    public void exitViewScope(RavelParser.ViewScopeContext ctx) {
        ((ViewSymbol)currentScope).createInterfaceType();
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

        VariableSymbol system = new VariableSymbol("system");
        system.setDefNode(ctx);
        system.setType(SystemType.INSTANCE);
        system.setWritable(false);
        currentScope.define(system);
    }

    @Override
    public void enterControllerVariableDefinition(RavelParser.ControllerVariableDefinitionContext ctx) {
        String name = ctx.Identifier().getText();
        Type type;
        if (ctx.type() != null)
            type = parseType(ctx.type());
        else
            type = null;

        RavelParser.Simple_expressionContext value = ctx.simple_expression();
        if (value.literal() != null) {
            Object literal = ParserUtils.literalToValue(value.literal());
            ConstantSymbol sym = new ConstantSymbol(name, literal);
            sym.setDefNode(ctx);

            Type constantType = ParserUtils.typeFromLiteral(literal);
            if (type != null) {
                if (!type.isAssignable(constantType)) {
                    emitError(ctx, "variable " + name + " cannot be assigned a value of type " + constantType.getName());
                }
                sym.setType(type);
            } else {
                sym.setType(constantType);
            }
            sym.setWritable(true);
            currentScope.define(sym);
        } else {
            String refName = value.qualified_name().getText();
            Symbol refSymbol = currentScope.getEnclosingScope().resolve(refName);
            if (!(refSymbol instanceof VariableSymbol)) {
                emitError(value.qualified_name(), "undeclared variable " + refName);
            } else {
                ReferenceSymbol ref = new ReferenceSymbol(name, refName);
                ref.setDefNode(ctx);

                Type refType = ((VariableSymbol) refSymbol).getType();
                if (type != null) {
                    if (!type.isAssignable(refType)) {
                        emitError(ctx, "variable " + name + " cannot be assigned a value of type " + refType.getName());
                    }
                    ref.setType(type);
                } else {
                    ref.setType(refType);
                }
                ref.setWritable(true);
                currentScope.define(ref);
            }
        }
    }

    @Override
    public void enterControllerArrayConstant(RavelParser.ControllerArrayConstantContext ctx) {
        String name = ctx.Identifier().getText();
        Type type = parseType(ctx.type());

        if (!(type instanceof ArrayType) || !(((ArrayType) type).getElementType() instanceof PrimitiveType)) {
            emitError(ctx, "only arrays of primitive types can be declared as constants");
            return;
        }
        ArrayType arrayType = (ArrayType)type;

        ArrayConstantSymbol sym = new ArrayConstantSymbol(name);
        sym.setType(new ArrayType(arrayType.getElementType()).makeImmutable());
        sym.setWritable(false);
        sym.setDefNode(ctx);
        currentScope.define(sym);

        for (RavelParser.LiteralContext value : ctx.literal()) {
            Object literal = ParserUtils.literalToValue(value);
            Type constantType = ParserUtils.typeFromLiteral(literal);
            if (constantType != arrayType.getElementType()) {
                literal = ParserUtils.convertLiterals(arrayType.getElementType(), constantType, literal);
                if (literal == null) {
                    emitError(ctx, "variable " + name + " cannot be assigned a value of type " + constantType.getName());
                }
            }
            sym.addValue(literal);
        }
        if (arrayType.isKnownBound()) {
            int bound = arrayType.getBound();
            if (bound < ctx.literal().size()) {
                emitError(ctx, "too many values for array of size " + bound);
            } else if (bound > ctx.literal().size()) {
                for (int i = ctx.literal().size(); i < bound; i++) {
                    switch ((PrimitiveType)(arrayType.getElementType())) {
                        case VOID:
                        case ANY:
                        case ERROR:
                            break;

                        case BOOL:
                            sym.addValue(false);
                            break;
                        case BYTE:
                            sym.addValue((byte)0);
                            break;
                        case INT32:
                            sym.addValue(0);
                            break;
                        case DOUBLE:
                            sym.addValue(0.0);
                            break;
                        case STR:
                            sym.addValue("");
                            break;
                        case ERROR_MSG:
                            sym.addValue(0);
                            break;
                        case TIMESTAMP:
                            sym.addValue(0);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void enterEventScope(RavelParser.EventScopeContext ctx) {
        //define event scope
        //define parameters in the scope
        String modelVarName = ctx.Identifier(0).getText();
        String eventName = ctx.Identifier(1).getText();

        EventHandlerSymbol es = new EventHandlerSymbol(modelVarName, eventName);

        // we create a local scope for each event
        // we must push the scope regardless of the errors we emit later because
        // we'll pop in exitEventScope()
        ctx.scope = es;
        es.setDefNode(ctx);
        currentScope.define(es);
        pushScope(es);

        Symbol modelVarSym = currentScope.resolve(modelVarName);

        if (!(modelVarSym instanceof VariableSymbol)) {
            emitError(ctx, "invalid event declaration " + es.getName() + ": does not refer to a declared model, source or sink");
            return;
        }
        Type classType =  ((VariableSymbol) modelVarSym).getType();
        if (!(classType instanceof ClassType)) {
            emitError(ctx, "invalid event declaration " + es.getName() + ": does not refer to a declared model, source or sink");
            return;
        }
        Type eventType = ((ClassType) classType).getMemberType(eventName);
        if (!(eventType instanceof EventType)) {
            emitError(ctx, "invalid event declaration " + modelVarName + "." + eventName + ": not a valid event name");
            return;
        }
        es.setType(eventType);

        if (((EventType) eventType).hasSelf()) {
            VariableSymbol selfVar = new VariableSymbol("self");
            // it's a stretch to say that the whole event defines self, but if
            // we ever need to emit type errors related to it, that's probably the
            // best AST node to attach to it
            selfVar.setDefNode(ctx);
            selfVar.setRegister(Registers.SELF_REG);
            selfVar.setType(((EventType) eventType).getArgumentTypes()[0]);
            selfVar.setWritable(false);
            currentScope.define(selfVar);
        }
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
            type = parseType(ctx.type());
        } else {
            type = PrimitiveType.ANY;
        }

        if (type instanceof ArrayType && ((ArrayType) type).isKnownBound())
            var.setWritable(false);
        else
            var.setWritable(true);
        var.setType(type);
        currentScope.define(var);
    }

    @Override
    public void enterTypedIdentDecl(RavelParser.TypedIdentDeclContext ctx) {
        String varName = ctx.Identifier().getText();
        VariableSymbol var = new VariableSymbol(varName);

        var.setType(parseType(ctx.type()));
        var.setDefNode(ctx);
        var.setWritable(false);
        currentScope.define(var);
    }

    @Override
    public void enterCast_op(RavelParser.Cast_opContext ctx) {
        ctx.computedType = parseType(ctx.type());
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
    public void enterForStatement(RavelParser.ForStatementContext ctx) {
        // push a scope for the for control
        LocalScope ls = new LocalScope("for_stmt_" + nextBlockId++, currentScope);
        ls.setDefNode(ctx);
        ctx.scope = ls;
        currentScope.nest(ls);
        pushScope(ls);
    }

    @Override
    public void exitForStatement(RavelParser.ForStatementContext ctx) {
        popScope();
    }

    @Override
    public void enterCLikeForStatement(RavelParser.CLikeForStatementContext ctx) {
        // push a scope for the for control
        LocalScope ls = new LocalScope("for_stmt_" + nextBlockId++, currentScope);
        ls.setDefNode(ctx);
        ctx.scope = ls;
        currentScope.nest(ls);
        pushScope(ls);
    }

    @Override
    public void exitCLikeForStatement(RavelParser.CLikeForStatementContext ctx) {
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
        String currentScopeName = currentScope.getName();

        boolean allowLiteral = currentScopeName.equals("implementation") ||
                currentScopeName.equals("properties") ||
                currentScopeName.equals("configuration");
        boolean allowReference = currentScopeName.equals("properties") ||
                currentScopeName.equals("configuration") ||
                currentScopeName.equals("platform") ||
                currentScopeName.equals("uses");
        boolean allowTypeReference = currentScopeName.equals("uses");
        boolean allowUnboundedReference = currentScopeName.equals("platform");

        String name = ctx.qualified_name().getText();
        RavelParser.Simple_expressionContext value = ctx.simple_expression();
        if (value.literal() != null ) {
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
            if (allowReference) {
                String refName = value.qualified_name().getText();
                Symbol refSymbol = currentScope.getEnclosingScope().resolve(refName);
                if (allowTypeReference) {
                    if (!(refSymbol instanceof TypeSymbol)) {
                        emitError(value.qualified_name(), refName + " does not refer to a valid model");
                    } else {
                        Type definedType = ((TypeSymbol) refSymbol).getDefinedType();
                        if (!(definedType instanceof ModelType)) {
                            emitError(value.qualified_name(), refName + " does not refer to a valid model");
                        } else {
                            ReferenceSymbol ref = new ReferenceSymbol(name, refName);
                            ref.setDefNode(ctx);
                            ref.setType(definedType);
                            currentScope.define(ref);
                        }
                    }
                } else if (!allowUnboundedReference && !(refSymbol instanceof VariableSymbol)) {
                    emitError(value.qualified_name(), "undeclared variable " + refName);
                } else {
                    ReferenceSymbol ref = new ReferenceSymbol(name, refName);
                    ref.setDefNode(ctx);
                    if (refSymbol instanceof VariableSymbol)
                        ref.setType(((VariableSymbol) refSymbol).getType());
                    else
                        ref.setType(PrimitiveType.ANY);
                    currentScope.define(ref);
                }
            } else {
                emitError(value.qualified_name(), "reference to a component parameter not allowed in this context");
            }
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
                    emitError(ctx, ">> " + pname + " with reference to " +pval.qualified_name().getText() +" << does not name a variable in the scope");
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

    @Override public void enterInterfaceInstantiation(RavelParser.InterfaceInstantiationContext ctx) {
        LocalScope ls = new LocalScope("interfaces", currentScope);
        ctx.scope = ls;
        ls.setDefNode(ctx);
        currentScope.nest(ls);
        pushScope(ls);
    }
    @Override public void exitInterfaceInstantiation(RavelParser.InterfaceInstantiationContext ctx) {
        for (Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addInterface(re.getName(),(InstanceSymbol) re);
        }
        popScope();
    }

    @Override public void enterViewInstantiation(RavelParser.ViewInstantiationContext ctx) {
        LocalScope ls = new LocalScope("views", currentScope);
        ctx.scope = ls;
        ls.setDefNode(ctx);
        currentScope.nest(ls);
        pushScope(ls);
    }
    @Override public void exitViewInstantiation(RavelParser.ViewInstantiationContext ctx) {
        for (Symbol re: ctx.scope.getSymbols()) {
            ((SpaceSymbol) currentScope.getEnclosingScope()).addView(re.getName(),(InstanceSymbol) re);
        }
        popScope();
    }

    @Override public void exitSpaceScope(RavelParser.SpaceScopeContext ctx) {
        popScope();
    }

    /**
     * The end of the file and the end of the App
     * TODO: handle multiple files and imports, design module system
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

