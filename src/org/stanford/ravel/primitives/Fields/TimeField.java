package org.stanford.ravel.primitives.Fields;

/**
 * this is parent class for all the time related subclasses
 * Created by lauril on 8/30/16.
 */
public abstract class TimeField<String> extends Field {

    protected String value;

    @Override
    public String getDefaultValue() {
        return (String) mDefaultValue;
    }


    public static abstract class Builder
            <T extends TimeField, B extends Builder<T, B>> extends Field.Builder<T, B>{
        //time field specific methods

    }
}