package ai.harmony.ravel.compiler.symbol;

import ai.harmony.ravel.antlr4.RavelParser;

/**
 * Created by lauril on 8/25/16.
 */
public class ControllerSymbol extends ComponentSymbol {


    public ControllerSymbol(String name) {
        super(name);
        super.setTypeIndex(RavelParser.CONTROLLER);
    }
}