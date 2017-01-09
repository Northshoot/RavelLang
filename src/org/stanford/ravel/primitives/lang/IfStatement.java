package org.stanford.ravel.primitives.lang;

import org.antlr.v4.misc.OrderedHashMap;

import java.util.Map;

/**
 * Created by lauril on 9/5/16.
 */
public class IfStatement extends CondStatement {
    protected String mIfCondition;
    protected Map<String, String> mBody;

    public IfStatement(){ mBody= new OrderedHashMap<>();}

    public static class Builder{
        protected IfStatement varObj;
        protected Builder thisObj;

        public Builder(){
            varObj = new IfStatement();
            thisObj = this;
        }

        public IfStatement build(){ return varObj; }
        public Builder setIfCondition(String condition){ varObj.mIfCondition = condition; return thisObj; }
        //public Builder setIfBody


    }
}
