package ai.harmony.ravel.primitives.Fields;

/**
 * Has applicaiton related context
 * TODO: actual implementation
 * Created by lauril on 8/30/16.
 */
public class BooleanField<Boolean> extends Field {
    private int baz;

    @Override
    public Boolean getDefaultValue() {
        return (Boolean) mDefaultValue;
    }


    public static final class Builder extends Field.Builder<BooleanField, BooleanField.Builder> {

        protected BooleanField createObject() {
            return new BooleanField();
        }

        protected BooleanField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public BooleanField.Builder baz(int baz) {
            obj.baz = baz;
            return thisObj;
        }
    }
}
