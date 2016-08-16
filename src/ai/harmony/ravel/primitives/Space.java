package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;

/**
 * Created by lauril on 7/29/16.
 */
public class Space extends Primitive{
    RavelParser.SpaceDeclarationContext ctx;
    public Space(RavelParser.SpaceDeclarationContext ctx){
        this.ctx = ctx;
    }

}
