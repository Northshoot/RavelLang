package ai.harmony.ravel.compiler;

import ai.harmony.ravel.RavelApplication;
import ai.harmony.ravel.antlr4.RavelBaseListener;
import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.exceptions.NoSuchBlockSymbolException;
import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
import ai.harmony.ravel.compiler.scopes.GlobalScope;
import ai.harmony.ravel.compiler.scopes.Scope;
import ai.harmony.ravel.compiler.symbols.*;
import ai.harmony.ravel.primitives.Model;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by lauril on 8/17/16.
 */
public class DefPhase extends RavelBaseListener {
    ParseTreeProperty<Scope> scopes = new ParseTreeProperty<>();
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

    void saveScope(ParserRuleContext ctx, Scope s) {
        scopes.put(ctx, s);
    }

    void prettyPrint(String s){
        System.out.println(getTab() + s);
    }
    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        ravelApp = new RavelApplication();
        globalScope = new GlobalScope(null);
        currentScope = globalScope;
        System.out.println("Entering enterFile_input");
    }


    @Override
    public void enterModelScope(RavelParser.ModelScopeContext ctx) {
        String name = ctx.identifier().getText();
        String type = ctx.modelType().getText();
        prettyPrint("Entering model: " + name + " of type " + type + " with args " + ctx.parameters().getText());
        ModelSymbol modelScope = new ModelSymbol(name, Symbol.Type.MODEL, type, currentScope);
        currentScope.define(modelScope);
        saveScope(ctx, modelScope);
        currentScope = modelScope;
    }

    @Override
    public void enterPropertiesScope(RavelParser.PropertiesScopeContext ctx) {
        intend++;
        prettyPrint("Entering PropertiesScope");
        //can only be defined ONCE per model, through error otherwise
    }


    @Override
    public void exitPropertiesScope(RavelParser.PropertiesScopeContext ctx) {
        intend--;
        prettyPrint("exit PropertiesScope");
    }

    @Override
    public void enterSchemaScope(RavelParser.SchemaScopeContext ctx) {
        intend++;
        //can only be defined ONCE per model, through error otherwise

    }

    @Override
    public void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        intend++;
        prettyPrint("Field: " + ctx.identifier().getText() + " type: " + ctx.field_type().getText() + " args: " + ctx.parameters().getText());
        if(currentScope instanceof SchemaSymbol) {
            String name = ctx.identifier().getText();
            currentScope.define(new FieldSymbol(
                    name,ctx.field_type().getText(),  Symbol.getType(ctx.field_type().start.getType())
            ));
            prettyPrint("field: " + name);
        } else {
            System.err.println(getTab()+"Can not define fields outside the schema");
        }
    }

    @Override
    public void exitFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        intend--;
    }
    @Override
    public void exitSchemaScope(RavelParser.SchemaScopeContext ctx) {
        intend--;
        prettyPrint("Exit SchemaScope");
    }

    @Override
    public void exitModelScope(RavelParser.ModelScopeContext ctx) {
        prettyPrint("Exit exitModelDeclaration");
//        ((ModelSymbol)currentScope).makeObjects();
//        Model m = ((ModelSymbol)currentScope).getModel();
//        ravelApp.addModel(m.getName(), m);
        currentScope = currentScope.getEnclosingScope();
        intend--;
    }

    @Override
    public void enterControllerScope(RavelParser.ControllerScopeContext ctx) {
        prettyPrint("Entering enterControllerDeclaration");
        intend++;
        String name = ctx.identifier().getText();
        ControllerSymbol controllerScope = new ControllerSymbol(name, Symbol.Type.CONTROLLER, currentScope);
        currentScope.define(controllerScope);
        saveScope(ctx,controllerScope);
        currentScope = controllerScope;
    }

    @Override
    public void enterEventScope(RavelParser.EventScopeContext ctx) {
        prettyPrint("Event Scope");
        intend++;
        //define event scope
        //define parameters in the scope
    }

    @Override
    public void enterQueryOperations(RavelParser.QueryOperationsContext ctx) { }

    @Override
    public void exitQueryOperations(RavelParser.QueryOperationsContext ctx) { }

    @Override
    public void exitEventScope(RavelParser.EventScopeContext ctx) {
        intend--;
    }

    @Override
    public void exitControllerScope(RavelParser.ControllerScopeContext ctx) {
        intend--;
        prettyPrint("Exit exitControllerDeclaration");
        currentScope = currentScope.getEnclosingScope();

    }

    @Override
    public void enterVarAssigment(RavelParser.VarAssigmentContext ctx) {
        prettyPrint("enterVarAssigment@ ident: " + ctx.identifier().getText() + " value: " + ctx.tdefvar().getText());
    }

    @Override
    public void exitVarAssigment(RavelParser.VarAssigmentContext ctx) {

    }
    @Override
    public void enterReferenceAssigment(RavelParser.ReferenceAssigmentContext ctx) {
        prettyPrint("enterReferenceAssigment@ ident: " + ctx.identifier().getText() + " value: " + ctx.dotted_name().getText());
    }

    @Override
    public void exitReferenceAssigment(RavelParser.ReferenceAssigmentContext ctx) { }

    @Override public void enterSpaceScope(RavelParser.SpaceScopeContext ctx) { }

    @Override public void enterPlatformScope(RavelParser.PlatformScopeContext ctx) { }

    @Override public void exitPlatformScope(RavelParser.PlatformScopeContext ctx) { }


    @Override public void enterModelInstanciation(RavelParser.ModelInstanciationContext ctx) { }

    @Override public void exitModelInstanciation(RavelParser.ModelInstanciationContext ctx) { }

    @Override public void enterControllerInstanciation(RavelParser.ControllerInstanciationContext ctx) { }

    @Override public void exitControllerInstanciation(RavelParser.ControllerInstanciationContext ctx) { }

    @Override public void enterSinkLinks(RavelParser.SinkLinksContext ctx) { }
    @Override public void exitSinkLinks(RavelParser.SinkLinksContext ctx) { }


    @Override public void enterSourceLinks(RavelParser.SourceLinksContext ctx) { }

    @Override public void exitSourceLinks(RavelParser.SourceLinksContext ctx) { }


    @Override public void enterInstance(RavelParser.InstanceContext ctx) { }

    @Override public void exitInstance(RavelParser.InstanceContext ctx) { }



    @Override public void exitSpaceScope(RavelParser.SpaceScopeContext ctx) { }



    /**
     * The end of the file and the end of the app
     * TODO: hadle multiple files and imports
     * @param ctx
     */
    @Override
    public void exitFile_input(RavelParser.File_inputContext ctx) {
        System.out.println(getTab()+"Exit exitFile_input");
        System.out.println(globalScope);
        walked = true;
        intend--;
    }

    private String getTab(){
        String tab="";
        for(int i=0; i<intend; i++){
            tab+="\t";
        }
        return tab;
    }
}

