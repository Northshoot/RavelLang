package org.stanford.ravel.primitives.Fields;

/**
 * Created by lauril on 8/30/16.
 */
public class DateTimeField extends TimeField {


    public static final class Builder extends TimeField.Builder<DateTimeField, DateTimeField.Builder> {

        protected DateTimeField createObject() {
            return new DateTimeField();
        }

        protected DateTimeField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public DateTimeField.Builder setTime(String baz) {
            obj.value = baz;
            return thisObj;
        }
    }
}
