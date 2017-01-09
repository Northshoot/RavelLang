package org.stanford.ravel.primitives.Fields;

/**
 * Created by lauril on 8/30/16.
 */
public class IntegerField<Integer> extends Field {
    private int baz;

    @Override
    public Integer getDefaultValue() {
        return (Integer) mDefaultValue;
    }


    public static final class Builder extends Field.Builder<IntegerField, IntegerField.Builder> {

        protected IntegerField createObject() {
            return new IntegerField();
        }

        protected IntegerField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public IntegerField.Builder baz(int baz) {
            obj.baz = baz;
            return thisObj;
        }
    }
}
