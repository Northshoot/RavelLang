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

    void saveScope(ParserRuleContext ctx, Scope s) {
        scopes.put(ctx, s);
    }

    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        ravelApp = new RavelApplication();
        globalScope = new GlobalScope();
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
        String blockTypeName = ctx.declType().getText();
        int blockType = ctx.declType().start.getType();
        System.out.println("Entering enterBlockSuite: " + blockTypeName);
        System.out.println("Current scope: " + currentScope);
        BlockSymbol blockScope;
        try {
            blockScope = BlockSymbolFactory.getBlockSymbol(blockType,  blockTypeName, currentScope);
            currentScope.define(blockScope);
            saveScope(ctx,blockScope);
            currentScope = blockScope;

        } catch (NoSuchBlockSymbolException e){
            int line = ctx.declType().start.getLine();
            int charp = ctx.declType().start.getCharPositionInLine();
            System.err.println("Error " + e.getMessage() + " on line: " + line + " at position " + charp + " ");
        } catch (SymbolNotAllowedInScopeException e) {
            int line = ctx.declType().start.getLine();
            int charp = ctx.declType().start.getCharPositionInLine();
            System.err.println("Error " + e.getMessage() + " on line: " + line + " at position " + charp + " ");

        }
    }

    @Override
    public void enterPrimitiveAssig(RavelParser.PrimitiveAssigContext ctx) {
        System.out.println("Entering enterPrimitiveAssig");
        /**
         * Create assignment scope and add it
         */

    }

    @Override
    public void exitPrimitiveAssig(RavelParser.PrimitiveAssigContext ctx) {
        System.out.println("Exitint exitPrimitiveAssig ");
    }

    @Override
    public void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        System.out.println("Entering enterFieldDeclaration ");
//        Field field = new Field(ctx);
//        currentScope.getPrimitive().addField(field);

    }


    @Override
    public void exitFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        System.out.println("Exit exitFieldDeclaration ");

    }

    @Override
    public void exitBlockSuite(RavelParser.BlockSuiteContext ctx ){
        System.out.println("Exit exitBlockSuite");
        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void enterControllerDeclaration(RavelParser.ControllerDeclarationContext ctx) {
        System.out.println("Entering enterControllerDeclaration");
        String name = ctx.NAME().getText();
        ControllerSymbol controllerScope = new ControllerSymbol(name, Symbol.Type.MODEL, currentScope);
        currentScope.define(controllerScope);
        saveScope(ctx,controllerScope);
        currentScope = controllerScope;
    }

    public void enterSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) {
        System.out.println("Entering enterSpaceDeclaration ");
        String name = ctx.NAME().getText();
        SpaceSymbol spaceScope = new SpaceSymbol(name, Symbol.Type.MODEL, currentScope);
        currentScope.define(spaceScope);
        saveScope(ctx,spaceScope);
        currentScope = spaceScope;
    }


    /**
      * Exit all components
      *
      */


    @Override
    public void exitModelDeclaration(RavelParser.ModelDeclarationContext ctx) {
        System.out.println("Exit exitModelDeclaration");
        currentScope = currentScope.getEnclosingScope();
    }

    @Override public void exitControllerDeclaration(RavelParser.ControllerDeclarationContext ctx) {
        System.out.println("Exit exitControllerDeclaration");
        currentScope = currentScope.getEnclosingScope();
    }
    @Override public void exitSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) {
        System.out.println("Exit exitSpaceDeclaration");
       currentScope = currentScope.getEnclosingScope();
    }
    @Override
    public void exitFile_input(RavelParser.File_inputContext ctx) {
        System.out.println("Exit exitFile_input");
        System.out.println(ravelApp);
    }

}
