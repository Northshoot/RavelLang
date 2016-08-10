package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelBaseVisitor;
import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.IR;
import ai.harmony.ravel.compiler.SymbolTable;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by lauril on 7/21/16.
 */
public class PrimitiveVisitor extends RavelBaseVisitor<Void> {
    IR ir;

    public PrimitiveVisitor(IR ir){
        ir = ir;

    }

    @Override
    public Void visitModelDeclaration(RavelParser.ModelDeclarationContext ctx) {
        //ctx.modelType().getText()
        System.out.println("Got Model with type: " +  ctx.modelType().getText() + " with name: " + ctx.NAME());
        return null;
    }

    @Override
    public Void visitControllerDeclaration(RavelParser.ControllerDeclarationContext ctx){
        System.out.println("Got Controller name: " + ctx.NAME());

        return null;
    }
    @Override
    public Void visitSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) {
        System.out.println("Got Space name: " + ctx.NAME());
        return null;
    }

    @Override
    public Void visitSuiteDecl(RavelParser.SuiteDeclContext cxt){

        System.out.println("block def: " + cxt.block_def().toString());

        return null;

    }
}
