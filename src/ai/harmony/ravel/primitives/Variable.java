package ai.harmony.ravel.primitives;

/**
 * Variable representation
 * Created by lauril on 9/5/16.
 */
public class Variable<T> {
    protected String mName;
    protected String mStringValue;
    protected String mStringType;
    protected T mValue;

    protected Variable(){}


    public static class Builder {
        protected Variable varObj;
        protected Builder thisObj;

        public Builder(){
            varObj = new Variable();
            thisObj = this;
        }

        public Variable build(){ return varObj; }
        public Builder name(String name){ varObj.mName = name; return thisObj; }
        public Builder stringValue(String name){ varObj.mStringValue = name; return thisObj; }
        public Builder stringType(String name){ varObj.mStringType = name; return thisObj; }
        public Builder value(boolean value){ varObj.mValue = value; return thisObj; }
        public Builder value(int value){ varObj.mValue = value; return thisObj; }
        public Builder value(float value){ varObj.mValue = value; return thisObj; }
        public Builder value(String value){ varObj.mValue = value; return thisObj; }

    }

    @Override
    public String toString(){
        return"Variable: " + mName + " [sType:" + mStringType
                + ",jType:" + mValue.getClass().getName() + ",value:" + mValue + "]";
    }



}
