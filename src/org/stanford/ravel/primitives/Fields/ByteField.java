package org.stanford.ravel.primitives.Fields;


/**
 * Created by lauril on 8/28/16.
 */
public final class ByteField<Byte> extends Field {
    private int baz;

    @Override
    public Byte getDefaultValue() {
        return (Byte) mDefaultValue;
    }


    public static final class Builder extends Field.Builder<ByteField, Builder> {

        protected ByteField createObject() {
            return new ByteField();
        }

        protected Builder thisObject() {
            return this;
        }

        //add all field specific setters
        public Builder baz(int baz) {
            obj.baz = baz;
            return thisObj;
        }
    }
}