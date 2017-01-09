package org.stanford.ravel.primitives.Fields;

/**
 * Created by lauril on 8/30/16.
 */
public class DateField extends TimeField {


    public static final class Builder extends TimeField.Builder<DateField, DateField.Builder> {

        protected DateField createObject() {
            return new DateField();
        }

        protected DateField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public DateField.Builder setTime(String baz) {
            obj.value = baz;
            return thisObj;
        }
    }
}
