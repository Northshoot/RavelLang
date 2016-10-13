package ai.harmony.ravel.primitives;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.lang.ConcreteLanguage;
import ai.harmony.api.platforms.ConcretePlatform;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by lauril on 9/7/16.
 */
public class Platform {

    private static Logger LOGGER = Logger.getLogger(Platform.class.getName());
    protected String mName;
    protected String mSystem;
    protected String mLang;
    protected String mTemplate;

    private ConcretePlatform mPlatform;
    private ConcreteLanguage mLanguage;
    private String path;

    //we instantiate a real platform that will provide API

    protected Platform() {
    }

    protected void makePlatform(){
        //create a real platform object from the data
        try {
            String[] pAPI = mSystem.split("\\.");
            String platform = pAPI[0];
            LOGGER.info("Building language: " + mLang + " for system: " + mSystem);
            mLanguage = (ConcreteLanguage) Class.forName("ai.harmony.api.lang." + mLang).newInstance();
            mPlatform = (ConcretePlatform) Class.forName("ai.harmony.api.platforms." + platform).newInstance();
            mPlatform.setAPILevel(pAPI[1]);

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

    public List<FileObject> buildLanguage(Space s) {
        return mLanguage.build(s);
    }

    public List<FileObject> buildPlatform(Space s) {
        return mPlatform.build(s);
    }

    public void setPath(String path) {
        this.path = path;
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
        public Builder language(String lang){varObj.mLang=lang; return thisObj; }
        public Builder system(String sys){varObj.mSystem=sys; return thisObj; }
        public Builder template(String tempalte){varObj.mTemplate =tempalte; return thisObj; }




    }
}
