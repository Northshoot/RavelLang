package ai.harmony.ravel.compiler.symbol;

import ai.harmony.ravel.primitives.Model;

/**
 * Created by lauril on 8/25/16.
 */
public class ModelScope extends SymbolWithScope {
    Model.Type mModelType;
    boolean mInstantiated = false;
    boolean mHasconcreateClass = false;

    public ModelScope(String name, Model.Type mt) {
        super(name);
        this.mModelType = mt;
    }
}
