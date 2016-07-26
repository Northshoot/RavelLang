package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr.RavelBaseListener;
import ai.harmony.ravel.antlr.RavelParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by lauril on 7/21/16.
 */
public class PrimitiveListner extends RavelBaseListener {
    ParseTreeProperty<String> ravel = new ParseTreeProperty<String>();

    public String getRavel(ParseTree ctx) {
        return ravel.get(ctx);
    }
    public void exitFile_input(RavelParser.File_inputContext ctx) {

        System.out.println("Exit Ravel file");
    }

    public void enterModel(RavelParser.ModelContext ctx) {
        RavelParser.ModelDeclContext mcntx = ctx.modelDecl();
        RavelParser.ModelPropContext mpcnt = ctx.modelP
        System.out.println("Enter Model, type " + mcntx.modelType().getText());
    }

    public void enterController(RavelParser.ControllerContext ctx) {

        System.out.println("Enter Controller:" + ctx.toStringTree());
    }

    public void enterViews(RavelParser.ViewsContext ctx) {

        System.out.println("Enter View:" + ctx.toStringTree());
    }

    public void enterTransform(RavelParser.TransformContext ctx) {

        System.out.println("Enter Transform:" + ctx.toStringTree());
    }

    public void enterSpace(RavelParser.SpaceContext ctx) {

        System.out.println("Enter Space:" + ctx.toStringTree());
    }
}
