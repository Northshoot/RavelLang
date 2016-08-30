package ai.harmony.ravel.primitives.Fields;

/**
 * Created by lauril on 8/30/16.
 */
public class StringField<String> extends Field {
    private int baz;

    @Override
    public String getDefaultValue() {
        return (String) mDefaultValue;
    }


    public static final class Builder extends Field.Builder<StringField, StringField.Builder> {

        protected StringField createObject() {
            return new StringField();
        }

        protected StringField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public StringField.Builder baz(int baz) {
            obj.baz = baz;
            return thisObj;
        }
    }
}
