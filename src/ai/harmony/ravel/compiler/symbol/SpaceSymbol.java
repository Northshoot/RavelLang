package ai.harmony.ravel.compiler.symbol;

import ai.harmony.antlr4.RavelParser;

/**
 * Created by lauril on 8/25/16.
 */
public class SpaceSymbol extends ComponentSymbol {

    public SpaceSymbol(String name) {
        super(name);
        super.setTypeIndex(RavelParser.SPACE);
    }
}

