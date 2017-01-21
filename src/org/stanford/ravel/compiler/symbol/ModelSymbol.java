package org.stanford.ravel.compiler.symbol;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.primitives.Model;

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
    }



    public Model.Type getType(){
        return this.mModelType;
    }
}
