package ai.harmony.ravel.primitives.Fields;

/**
 * Created by lauril on 8/30/16.
 */
public class DecimalField<Decimal> extends Field {
    private int baz;

    @Override
    public Decimal getDefaultValue() {
        return (Decimal) mDefaultValue;
    }


    public static final class Builder extends Field.Builder<DecimalField, DecimalField.Builder> {

        protected DecimalField createObject() {
            return new DecimalField();
        }

        protected DecimalField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public DecimalField.Builder baz(int baz) {
            obj.baz = baz;
            return thisObj;
        }
    }
}
