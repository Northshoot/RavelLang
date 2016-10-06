package ai.harmony.ravel.primitives;

import ai.harmony.api.platforms.SystemApi;
import ai.harmony.ravel.primitives.lang.IfStatement;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 9/7/16.
 */
public class Platform {

    private String path;
    protected String mName;
    protected String mSystem;
    protected String mLanguage;
    protected String mTempalte;
    protected Map<String, String> mSource;
    protected Map<String, String> mSink;
    private Object mPlatform;

    //we instantiate a real platform that will provide API

    protected Platform() {
        mSource = new LinkedHashMap<>();
        mSink = new LinkedHashMap<>();
    }

    protected void makePlatform(){
        //create a real platform object from the data
        try {
            String[] pAPI = mSystem.split("\\.");
            mPlatform = Class.forName("ai.harmony.api.platforms." + pAPI[0]).newInstance();
            ((SystemApi)mPlatform).setAPILevel(pAPI[1]);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        //TODO: resolve existence of the references to sinks and sources

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
        public Builder template(String tempalte){varObj.mTempalte=tempalte; return thisObj; }

        public Builder sink(String name, String resolve){varObj.mSource.put(name,resolve); return thisObj; }
        public Builder source(String name, String resolve){varObj.mSink.put(name, resolve); return thisObj; }



    }
}
