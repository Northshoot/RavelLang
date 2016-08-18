package ai.harmony.ravel.compiler.scopes;

import ai.harmony.ravel.primitives.ModelType;
import ai.harmony.ravel.primitives.Primitive;

/**
 * Created by lauril on 8/18/16.
 */
public class ModelScope extends BaseScope {

    String mName;
    ModelType mModelType;

    public ModelScope(String name, String type, Scope parent) {
        super(parent);
    }

    public Primitive getPrimitive(){
                return null;
        }
        public String getScopeName() {
                return "model";
        }
}
