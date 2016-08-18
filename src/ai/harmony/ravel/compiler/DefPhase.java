package ai.harmony.ravel.compiler;

import ai.harmony.ravel.antlr4.RavelBaseListener;
import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.scopes.GlobalScope;
import ai.harmony.ravel.compiler.scopes.ModelScope;
import ai.harmony.ravel.compiler.scopes.Scope;
import ai.harmony.ravel.compiler.symbols.ModelSymbol;
import ai.harmony.ravel.compiler.symbols.PrimitiveSymbol;
import ai.harmony.ravel.primitives.Field;
import ai.harmony.ravel.primitives.Model;
import ai.harmony.ravel.primitives.Primitive;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by lauril on 8/17/16.
 */
public class DefPhase extends RavelBaseListener {
    ParseTreeProperty<Primitive> scopes = new ParseTreeProperty<>();
    RavelApplication ravelApp;
    Scope currentScope;
    GlobalScope  globalScope;

    @Override
    public void enterFile_input(RavelParser.File_inputContext ctx) {
        ravelApp = new RavelApplication();
        globalScope = new GlobalScope();
        currentScope = globalScope;
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
        String name = ctx.NAME().getText();
        String mType = ctx.modelType().getText();
        ModelSymbol modelScope = new ModelSymbol(name, mType, currentScope);
        currentScope.define(modelScope);
    }
    @Override
    public void enterBlockSuite(RavelParser.BlockSuiteContext ctx) {
        String type = ctx.declType().getText();
        /**
         * Check if the type is allowed in this scope
         */

    }
    @Override
    public void enterAssignmentDec(RavelParser.AssignmentDecContext ctx) {
        /**
         * Create assignment scope and add it
         */

    }
    @Override
    public void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        Field field = new Field(ctx);
        currentScope.getPrimitive().addField(field);

    }


    @Override
    public void exitFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void exitAssignmentDec(RavelParser.AssignmentDecContext ctx) { }

    @Override
    public void exitBlockSuite(RavelParser.BlockSuiteContext ctx ){ }

    @Override
    public void enterControllerDeclaration(RavelParser.ControllerDeclarationContext ctx) { }

    public void enterSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) { }


    /**
      * Exit all components
      *
      */


    @Override
    public void exitModelDeclaration(RavelParser.ModelDeclarationContext ctx) {
        System.out.println(currentScope);
        currentScope = currentScope.getEnclosingScope();
    }

    @Override public void exitControllerDeclaration(RavelParser.ControllerDeclarationContext ctx) {
        System.out.println(currentScope);
        currentScope = currentScope.getEnclosingScope();
    }
    @Override public void exitSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) {
        System.out.println(currentScope);
        currentScope = currentScope.getEnclosingScope();
    }
    @Override
    public void exitFile_input(RavelParser.File_inputContext ctx) {

        System.out.println(ravelApp);
    }

}
