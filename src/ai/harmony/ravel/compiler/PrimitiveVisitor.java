package ai.harmony.ravel.compiler;

import ai.harmony.ravel.antlr4.RavelBaseVisitor;
import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.primitives.Controller;
import ai.harmony.ravel.primitives.Model;
import ai.harmony.ravel.primitives.ModelType;

/**
 * Created by lauril on 7/21/16.
 */
public class PrimitiveVisitor extends RavelBaseVisitor<Void> {
    IR ir;
    Model m;
    enum components {MODEL, CONROLLER, SPACE, FLOW}
    components current = null;

    public PrimitiveVisitor(IR ir){
        this.ir = ir;

    }

    @Override
    public Void visitModelDeclaration(RavelParser.ModelDeclarationContext ctx) {
        //ctx.modelType().getText()
        current = components.MODEL;
        m = new Model();
        m.setName(ctx.NAME().toString());
        m.setmModelType(new ModelType(ctx.modelType().getText()));
        ir.add(ctx.NAME().toString(), m);
        System.out.println("Got Model with type: " +  ctx.modelType().getText() + " with name: " + ctx.NAME());
        return visitChildren(ctx);
    }

    @Override
    public Void visitControllerDeclaration(RavelParser.ControllerDeclarationContext ctx){
        current = components.CONROLLER;
        ir.add(ctx.NAME().toString(), new Controller(ctx));
        System.out.println("Got Controller name: " + ctx.NAME());

        return visitChildren(ctx);
    }
    @Override
    public Void visitSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx) {
        current = components.SPACE;
        //ir.add(ctx.NAME().toString(), new Space(ctx));
        System.out.println("Got Space name: " + ctx.NAME());
        return visitChildren(ctx);
    }

    @Override
    public Void visitBlockSuite(RavelParser.BlockSuiteContext ctx) {
//        System.out.println("visitBlockSuite: " + ctx.decl_type().getText());
//        switch (current){
//            case MODEL:
//                break;
//            case CONROLLER:
//                break;
//            case SPACE:
//                break;
//
//            }

        return visitChildren(ctx);
    }


    @Override
    public Void visitFieldDeclaration(RavelParser.FieldDeclarationContext ctx) {
        System.out.println("visitFieldDeclaration: " + ctx.NAME());
        if (current != components.MODEL) {
            System.err.println("Cant have Field outside model!");
            return null;
        }
        return visitChildren(ctx);
    }
    @Override
    public Void visitArgs(RavelParser.ArgsContext ctx) {
        System.out.println("visitArg: " + ctx.getText().toString());
        return visitChildren(ctx);
    }

    @Override public Void visitArg(RavelParser.ArgContext ctx) {
        System.out.println("visitArg: " + ctx.getText().toString());
        return visitChildren(ctx); }

//    @Override
//    public Void visitSuiteDecl(RavelParser.SuiteDeclContext cxt){
//
//        System.out.println("block def: " + cxt.block_def().toString());
//
//        return null;
//
//    }
}
