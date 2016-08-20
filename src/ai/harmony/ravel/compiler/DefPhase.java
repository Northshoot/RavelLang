package ai.harmony.ravel.compiler;

import ai.harmony.ravel.antlr4.RavelBaseListener;
import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.exceptions.NoSuchBlockSymbolException;
import ai.harmony.ravel.compiler.exceptions.SymbolNotAllowedInScopeException;
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

    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        ravelApp = new RavelApplication();
        globalScope = new GlobalScope(null);
        currentScope = globalScope;
        System.out.println("Entering enterFile_input");
    }

    /**
     * We dont cate about component definitions, mostly for grammar
     * public void enterComp_def(RavelParser.Comp_defContext ctx) { }
     *
     */

    /**
     * First we enter a components which declares the current scope
     *
     */

    @Override
    public void enterModelDeclaration(RavelParser.ModelDeclarationContext ctx) {
        System.out.println("Entering enterModelDeclaration");
        String name = ctx.NAME().getText();
        ModelSymbol modelScope = new ModelSymbol(name, Symbol.Type.MODEL, currentScope);
        currentScope.define(modelScope);
        saveScope(ctx,modelScope);
        currentScope = modelScope;
    }
    @Override
    public void enterBlockSuite(RavelParser.BlockSuiteContext ctx) {
        intend++;
        String blockTypeName = ctx.declType().getText();
        int blockType = ctx.declType().start.getType();
        System.out.println(getTab() + "Entering enterBlockSuite: " + blockTypeName);
        BlockSymbol blockScope;
        try {
            blockScope = BlockSymbolFactory.getBlockSymbol(blockType,  blockTypeName, currentScope);
            currentScope.define(blockScope);
            saveScope(ctx, blockScope);
            currentScope = blockScope;
            System.out.println(getTab() +"blockScope: " + blockScope);
        } catch (NoSuchBlockSymbolException e){
            int line = ctx.declType().start.getLine();
            int charp = ctx.declType().start.getCharPositionInLine();
            System.err.println(getTab() +"Error " + e.getMessage() + " on line: " + line + " at position " + charp + " ");
        } catch (SymbolNotAllowedInScopeException e) {
            int line = ctx.declType().start.getLine();
            int charp = ctx.declType().start.getCharPositionInLine();
            System.err.println(getTab() +"Error " + e.getMessage() + " on line: " + line + " at position " + charp + " ");

        }
    }

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
//        if( currentScope instanceof BlockSymbol) {

            currentScope.define(new VarSymbol(varType, name,
                    Symbol.getType(ctx.primitive_type().start.getType())
            ));
//        } else {
//            //TODO: variables can only be defines in the
//            System.err.println(getTab() +"Should not end up there: ");
//            System.err.print(currentScope.getScopeName());
//        }


    }

    @Override
    public void exitVarAssig(RavelParser.VarAssigContext ctx) {

        System.out.println(getTab()+"Exit enterVarAssig ");
        intend--;
    }

    @Override
    public void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        intend++;
        System.out.println(getTab() +"Entering enterFieldDeclaration ");
        if(currentScope instanceof SchemaSymbol) {
            String name = ctx.NAME().getText();
            currentScope.define(new FieldSymbol(
                        name, Symbol.getType(ctx.field_type().start.getType())
            ));
            System.out.println(getTab()+"field: " + name);
        } else {
            System.err.println(getTab()+"Can not define fields outside the schema");
        }
    }


    @Override
    public void exitFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {

        System.out.println(getTab() +"Exit exitFieldDeclaration ");
        intend--;

    }

    @Override
    public void exitBlockSuite(RavelParser.BlockSuiteContext ctx ){
        System.out.println(getTab() + "Exit exitBlockSuite");
        currentScope = currentScope.getEnclosingScope();
        intend--;
    }

    @Override
    public void enterControllerDeclaration(RavelParser.ControllerDeclarationContext ctx) {
        System.out.println(getTab()+"Entering enterControllerDeclaration");
        intend++;
        String name = ctx.NAME().getText();
        ControllerSymbol controllerScope = new ControllerSymbol(name, Symbol.Type.MODEL, currentScope);
        currentScope.define(controllerScope);
        saveScope(ctx,controllerScope);
        currentScope = controllerScope;
    }
    @Override
    public void enterEventDecl(RavelParser.EventDeclContext ctx) {
        System.out.println(getTab()+"Enter enterEventDecl");
        if ( currentScope instanceof ControllerSymbol ){
            intend++;
            String name = ctx.comp().getText()+ctx.trigger().getText();
            EventSymbol eventScope = new EventSymbol(name, Symbol.Type.EVENT, currentScope);
            currentScope.define(eventScope);
            saveScope(ctx,eventScope);
            currentScope = eventScope;

        } else {
            System.err.println(getTab() + "Events only allowed in the controller");
        }
    }

    @Override
    public void exitEventDecl(RavelParser.EventDeclContext ctx) {
        System.out.println(getTab()+"Exit exitEventDecl");
        currentScope = currentScope.getEnclosingScope();
        intend--;
    }


    public void enterSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) {
        System.out.println("Entering enterSpaceDeclaration ");
        intend++;
        String name = ctx.NAME().getText();
        SpaceSymbol spaceScope = new SpaceSymbol(name, Symbol.Type.MODEL, currentScope);
        currentScope.define(spaceScope);
        saveScope(ctx,spaceScope);
        currentScope = spaceScope;
    }

    @Override
    public void enterInstansDecl(RavelParser.InstansDeclContext ctx) {
        if ( currentScope.getEnclosingScope() instanceof SpaceSymbol ){
            intend++;
            String name = ctx.NAME(0).getText();
            String instanceName = ctx.NAME(1).getText();
            InstantiationSymbol is = new InstantiationSymbol(name, instanceName);
            //TODO: skipping args for now
            currentScope.define(is);
            System.out.println(getTab() + "Entered Reference: " + is);
        } else {
            System.err.println(getTab() + "Components can only be instantiated in the space");
        }
    }


    @Override
    public void exitInstansDecl(RavelParser.InstansDeclContext ctx) {
        intend--;
    }

    @Override
    public void enterRefDecl(RavelParser.RefDeclContext ctx) {
        if ( currentScope.getEnclosingScope() instanceof SpaceSymbol ){
            intend++;
            String name = ctx.NAME(0).getText();
            String refered = ctx.NAME(1).getText();
            //TODO: skipping args for now
            ReferenceSymbol rs = new ReferenceSymbol(name, refered);
            currentScope.define(rs);
            System.out.println(getTab() + "Entered Reference: " + rs);

        } else {
            System.err.println(getTab() + "Reference can only made in space" + currentScope);
        }
    }


    @Override
    public void exitRefDecl(RavelParser.RefDeclContext ctx) { }

    /**
      * Exit all components
      *
      */


    @Override
    public void exitModelDeclaration(RavelParser.ModelDeclarationContext ctx) {
        System.out.println(getTab()+"Exit exitModelDeclaration");
        currentScope = currentScope.getEnclosingScope();
        intend--;
    }

    @Override public void exitControllerDeclaration(RavelParser.ControllerDeclarationContext ctx) {
        System.out.println(getTab()+"Exit exitControllerDeclaration");
        currentScope = currentScope.getEnclosingScope();
        intend--;
    }
    @Override public void exitSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) {
        System.out.println(getTab()+"Exit exitSpaceDeclaration");
       currentScope = currentScope.getEnclosingScope();
        intend--;
    }
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
