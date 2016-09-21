package ai.harmony.ravel.primitives;

import ai.harmony.ravel.primitives.lang.IfStatement;

/**
 * Created by lauril on 9/7/16.
 */
public class Platform {

    protected String mName;
    protected String mSystem;
    protected String mLanguage;

    //we instantiate a real platform that will provide API

    protected Platform() {}
    protected void makePlatform(){
        //create a real platform object from the data

    }
    public static class Builder {
        protected Platform varObj;
        protected Builder thisObj;

        public Builder(){
            varObj = new Platform();
            thisObj = this;
        }

        public Platform build(){
            varObj.makePlatform();
            return varObj;
        }
        public Builder name(String name) {varObj.mName = name; return thisObj; }
        public Builder language(String lang){varObj.mLanguage=lang; return thisObj; }
        public Builder system(String sys){varObj.mSystem=sys; return thisObj; }



    }
}
