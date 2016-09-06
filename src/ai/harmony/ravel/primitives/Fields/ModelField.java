package ai.harmony.ravel.primitives.Fields;

import ai.harmony.ravel.primitives.Model;

/**
 * a Field that refernces to another model
 * TODO: actual implementation
 * Created by lauril on 8/30/16.
 */
public class ModelField<String> extends Field {
    private Model mModel;

    @Override
    public String getDefaultValue() {
        return (String) mDefaultValue;
    }


    public static final class Builder extends Field.Builder<ModelField, ModelField.Builder> {

        protected ModelField createObject() {
            return new ModelField();
        }

        protected ModelField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public ModelField.Builder model(Model baz) {
            obj.mModel = baz;
            return thisObj;
        }
    }
}
