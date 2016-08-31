package ai.harmony.ravel.primitives.Fields;

/**
 * Has applicaiton related context
 * TODO: actual implementation
 * Created by lauril on 8/30/16.
 */
public class ContextField<String> extends Field {
    private int baz;

    @Override
    public String getDefaultValue() {
        return (String) mDefaultValue;
    }


    public static final class Builder extends Field.Builder<ContextField, ContextField.Builder> {

        protected ContextField createObject() {
            return new ContextField();
        }

        protected ContextField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public ContextField.Builder baz(int baz) {
            obj.baz = baz;
            return thisObj;
        }
    }
}
