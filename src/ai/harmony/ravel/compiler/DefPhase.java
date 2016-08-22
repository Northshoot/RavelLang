package ai.harmony.ravel.compiler;

import ai.harmony.ravel.antlr4.RavelBaseListener;
import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.scopes.GlobalScope;
import ai.harmony.ravel.compiler.scopes.Scope;
import ai.harmony.ravel.compiler.symbols.*;
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
    GlobalScope  globalScope;
    int intend = 0;

    void saveScope(ParserRuleContext ctx, Scope s) {
        scopes.put(ctx, s);
    }
    void exitScope(){
        System.out.println(getTab()+"Exit scope " + currentScope.getScopeName());
        currentScope = currentScope.getEnclosingScope();
        intend--;
    }
    void enterScope(String blockTypeName, Scope blockScope, ParserRuleContext ctx){
        intend++;
        System.out.println(getTab() + "Entering Scope: " + blockTypeName);
        currentScope.define((Symbol)blockScope);
        saveScope(ctx, blockScope);
        currentScope = blockScope;
    }

    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        ravelApp = new RavelApplication();
        globalScope = new GlobalScope(null);
        currentScope = globalScope;
        System.out.println("Entering enterFile_input");
    }

    /**
     * Deal with models
     */

    @Override
    public void enterModelDeclaration(RavelParser.ModelDeclarationContext ctx) {
        String name = ctx.NAME().getText();
        enterScope(name, new ModelSymbol(name, Symbol.Type.MODEL, currentScope), ctx );

    }
    @Override
    public void enterModelPropertyBlock(RavelParser.ModelPropertyBlockContext ctx) {
        String blockTypeName = ctx.PROPERTIES().getText();
        enterScope(blockTypeName,
                    new ModelPropertyBlock(blockTypeName, currentScope),
                    ctx);
    }
    @Override
    public void enterModelProperty(RavelParser.ModelPropertyContext ctx) {
        intend++;
        System.out.println(getTab() +"Entering enterModelProperty ");
        String name = ctx.model_property_opt().getText();
        currentScope.define(new ModelPropertySymbol(
                name, Symbol.Type.tINVALID,
                currentScope ));
        System.out.println(getTab()+"property: " + name);
    }

    @Override
    public void exitModelProperty(RavelParser.ModelPropertyContext ctx) { intend--; }
    @Override
    public void exitModelPropertyBlock(RavelParser.ModelPropertyBlockContext ctx) { exitScope();}

    //Create model schema scope accepting all fields
    @Override
    public void enterModelSchemaBlock(RavelParser.ModelSchemaBlockContext ctx) {
        String name = ctx.SCHEMA().getText();
        enterScope(name,
                new ModelSchemaBlock(name, currentScope),
                ctx);
    }

    //define all fields withing this model scope
    @Override
    public void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        intend++;
        System.out.println(getTab() +"Entering enterFieldDeclaration ");
            String name = ctx.NAME().getText();
            currentScope.define(new FieldSymbol(
                    name, Symbol.getType(ctx.field_type().start.getType())
            ));
            System.out.println(getTab()+"field: " + name);
    }


    @Override public void exitFieldDeclaration(RavelParser.FieldDeclarationContext ctx) { }
    @Override public void exitModelSchemaBlock(RavelParser.ModelSchemaBlockContext ctx) { exitScope();}
    @Override public void exitModelDeclaration(RavelParser.ModelDeclarationContext ctx) { exitScope();}

    /**
     * Deal with controllers
     */
    @Override
    public void enterControllerDeclaration(RavelParser.ControllerDeclarationContext ctx) {
        String name = ctx.NAME().getText();
        enterScope(name, new ControllerSymbol(name, Symbol.Type.CONTROLLER, currentScope), ctx );
    }

    @Override
    public void enterCntrConfigBlock(RavelParser.CntrConfigBlockContext ctx) {
        String blockTypeName = ctx.CONFIGURATION().getText();
        enterScope(blockTypeName,
                new ControllerConfigBlock(blockTypeName, currentScope),
                ctx);
    }

    @Override
    public void enterRefDecl(RavelParser.RefDeclContext ctx) {
            intend++;
            String name = ctx.NAME().getText();
            String refered = ctx.dotted_name().getText();
            //TODO: skipping args for now
            ReferenceSymbol rs = new ReferenceSymbol(name, refered);
            currentScope.define(rs);
            System.out.println(getTab() + "Entered Reference: " + rs);
    }
    @Override public void exitRefDecl(RavelParser.RefDeclContext ctx) { intend--; }

    @Override
    public void enterVarAssig(RavelParser.VarAssigContext ctx) {
        intend ++;
        System.out.println(getTab() +"Entering enterVarAssig");
        /**
         * Create assignment scope and add it
         */
        String varType = ctx.primitive_type().getText();
        String name = ctx.NAME().getText();
        System.out.println(getTab() + "Var declaration found: " + name + " of type " + varType);
        currentScope.define(new VarSymbol(varType, name,
                Symbol.getType(ctx.primitive_type().start.getType())
        ));
    }

    @Override public void exitVarAssig(RavelParser.VarAssigContext ctx) { intend--; }
    @Override public void exitCntrConfigBlock(RavelParser.CntrConfigBlockContext ctx) { exitScope(); }

    @Override
    public void enterEventDecl(RavelParser.EventDeclContext ctx) {
            String name = ctx.refName().getText()+"." + ctx.trigEvent().getText();
            enterScope(name, new EventSymbol(name, Symbol.Type.EVENT, currentScope), ctx);

    }

    @Override public void exitEventDecl(RavelParser.EventDeclContext ctx) { exitScope(); }
    @Override public void exitControllerDeclaration(RavelParser.ControllerDeclarationContext ctx) { exitScope(); }

    /**
     * Deal with spaces
     */
    public void enterSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) {
        String name = ctx.NAME().getText();
        enterScope(name, new SpaceSymbol(name, Symbol.Type.MODEL, currentScope), ctx);
    }

    @Override
    public void enterSpacePropertiesBlock(RavelParser.SpacePropertiesBlockContext  ctx) {
        //Create properties scope
        String name = ctx.PROPERTIES().getText();
        enterScope( name , new SpacePropertyBlock(
                name, currentScope), ctx);
    }

    @Override
    public void enterSpaceProperty(RavelParser.SpacePropertyContext ctx) {
        intend ++;
        System.out.println(getTab() +"Entering enterSpaceProperty");
        /**
         * define each property in the scope
         */
        String name = ctx.spaceProp_lang().getText();
        currentScope.define(new SpacePropertySymbol(name, Symbol.Type.BLOCK, currentScope));
    }

    @Override public void exitSpaceProperty(RavelParser.SpacePropertyContext ctx) { intend --; }
    @Override public void exitSpacePropertiesBlock(RavelParser.SpacePropertiesBlockContext ctx) { exitScope(); }

    @Override
    public void enterSpacePlatformBlock(RavelParser.SpacePlatformBlockContext ctx) {
        //Create space platform
        String name = ctx.PLATFORM().getText();
        enterScope( name , new SpacePlatformBlock(
                name, currentScope), ctx);
    }
    @Override public void enterPlatformTemplates(RavelParser.PlatformTemplatesContext ctx) {
        //get templates
        intend ++;
        System.out.println(getTab() +"Entering enterPlatformTemplates");
        String name = ctx.TEMPLATES().getText();
        currentScope.define(new SpacePlatformSymbol(name, Symbol.Type.BLOCK, currentScope));
    }

    @Override public void exitPlatformTemplates(RavelParser.PlatformTemplatesContext ctx) { intend--; }

    @Override public void enterPlatformAPI(RavelParser.PlatformAPIContext ctx) {
        //get api

        intend ++;
        System.out.println(getTab() +"Entering enterPlatformTemplates");
        String name = ctx.PLATFORM().getText();
        currentScope.define(new SpacePlatformSymbol(name, Symbol.Type.BLOCK, currentScope));
    }

    @Override public void exitPlatformAPI(RavelParser.PlatformAPIContext ctx) { intend--; }

    @Override public void enterPlatformEvent(RavelParser.PlatformEventContext ctx) {
        //add platform events
        intend ++;
        System.out.println(getTab() +"Entering enterPlatformTemplates");
        String name = ctx.NAME().getText();
        currentScope.define(new SpacePlatformSymbol(name, Symbol.Type.BLOCK, currentScope));
    }

    @Override public void exitPlatformEvent(RavelParser.PlatformEventContext ctx) { intend--; }

    @Override public void exitSpacePlatformBlock(RavelParser.SpacePlatformBlockContext ctx) { exitScope(); }

    @Override
    public void enterSpaceModelsBlock(RavelParser.SpaceModelsBlockContext ctx) {
        // crete model instantiation scope
        String name = ctx.MODELS().getText();
        enterScope( name , new SpacePlatformBlock(
                name, currentScope), ctx);
    }

    @Override public void exitSpaceModelsBlock(RavelParser.SpaceModelsBlockContext ctx) { exitScope(); }


    @Override
    public void enterInstansDecl(RavelParser.InstansDeclContext ctx) {
            intend++;
            String name = ctx.refName().getText();
            String instanceName = ctx.compName().getText();
            InstantiationSymbol is = new InstantiationSymbol(name, instanceName);
            //TODO: skipping args for now
            currentScope.define(is);
            System.out.println(getTab() + "Entered instancedeclaration: " + is);

    }

    @Override public void exitInstansDecl(RavelParser.InstansDeclContext ctx) { intend--; }
    @Override public void enterSpaceControllerBlock(RavelParser.SpaceControllerBlockContext ctx) {
        //Create controllers scope
        String name = ctx.CONTROLLERS().getText();
        enterScope( name , new SpacePlatformBlock(
                name, currentScope), ctx);

    }

    @Override public void exitSpaceControllerBlock(RavelParser.SpaceControllerBlockContext ctx) { exitScope(); }

    @Override public void enterSpaceSourceBlock(RavelParser.SpaceSourceBlockContext ctx) {
        //create courses
        String name = ctx.SOURCES().getText();
        enterScope( name , new SpacePlatformBlock(
                name, currentScope), ctx);
    }

    @Override public void exitSpaceSourceBlock(RavelParser.SpaceSourceBlockContext ctx) { exitScope(); }


    @Override public void enterSapceSinkBlock(RavelParser.SapceSinkBlockContext ctx) {
        //create sinks
        String name = ctx.SINKS().getText();
        enterScope( name , new SpacePlatformBlock(
                name, currentScope), ctx);
    }

    @Override public void exitSapceSinkBlock(RavelParser.SapceSinkBlockContext ctx) { exitScope(); }

    @Override public void exitSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) { exitScope(); }




    /**
     * Deal with spaces
     */
    @Override
    public void exitFile_input(RavelParser.File_inputContext ctx) {
        System.out.println(getTab()+"Exit exitFile_input");
        System.out.println(globalScope);
        //System.out.println(ravelApp);
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
