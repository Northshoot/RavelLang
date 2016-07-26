package ai.harmony.ravel;

import ai.harmony.ravel.antlr.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by lauril on 7/21/16.
 */
public class ModelListener extends RavelBaseListener {
    ParseTreeProperty<String> ravel = new ParseTreeProperty<String>();

    public String getRavel(ParseTree ctx) {
        return ravel.get(ctx);
    }

    public void exitFile_input(RavelParser.File_inputContext ctx) {

        System.out.println("Exit Ravel file");
    }

    public void enterModel(RavelParser.ModelContext ctx) {

        System.out.println("Enter Model:" + ctx.toStringTree());
    }
}