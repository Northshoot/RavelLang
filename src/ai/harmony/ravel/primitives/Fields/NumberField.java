package ai.harmony.ravel.primitives.Fields;

/**
 * Created by lauril on 8/30/16.
 */
public class NumberField<Decimal> extends Field {
    private int baz;

    @Override
    public Decimal getDefaultValue() {
        return (Decimal) mDefaultValue;
    }


    public static final class Builder extends Field.Builder<NumberField, NumberField.Builder> {

        protected NumberField createObject() {
            return new NumberField();
        }

        protected NumberField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public NumberField.Builder baz(int baz) {
            obj.baz = baz;
            return thisObj;
        }
    }
}
