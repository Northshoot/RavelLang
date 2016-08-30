package ai.harmony.ravel.primitives.Fields;

/**
 * Created by lauril on 8/30/16.
 */
public class TimeStampField extends TimeField {


    public static final class Builder extends TimeField.Builder<TimeStampField, TimeStampField.Builder> {

        protected TimeStampField createObject() {
            return new TimeStampField();
        }

        protected TimeStampField.Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public TimeStampField.Builder setTime(String baz) {
            obj.value = baz;
            return thisObj;
        }
    }
}
