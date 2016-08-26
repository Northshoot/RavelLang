package ai.harmony.ravel.compiler.symbol;

import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.primitives.Model;

/**
 * Created by lauril on 8/25/16.
 */
public class ModelSymbol extends ComponentSymbol {
    Model.Type mModelType;


    public ModelSymbol(String name, Model.Type mt) {
        super(name);
        // this is model type e.g.
        // local model assigned to embedded space
        // cannot  be accessed from any other space
        this.mModelType = mt;
        // this is the type of the component
        super.setTypeIndex(RavelParser.MODEL);
    }
}
