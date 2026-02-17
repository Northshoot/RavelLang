package ai.harmony.ravel.primitives;

import ai.harmony.ravel.RavelProperties;
import ai.harmony.ravel.api.lang.ConcreteLanguage;
import ai.harmony.ravel.api.platforms.ConcretePlatform;

import java.util.logging.Logger;

/**
 * Created by lauril on 9/7/16.
 */
public class Platform {
    private static Logger LOGGER = Logger.getLogger(Platform.class.getName());

    private final String mSystem;
    private final String mLang;

    private ConcretePlatform mPlatform;
    private ConcreteLanguage mLanguage;

    //we instantiate a real platform that will provide API

    private Platform(String system, String lang) {
        this.mSystem = system;
        this.mLang = lang;
    }

    public ConcretePlatform getConcretePlatform() {
        return mPlatform;
    }
    public ConcreteLanguage getConcreteLanguage() {
        return mLanguage;
    }

    public void realize() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        //create a real platform object from the data
        String[] pAPI = mSystem.split("\\.");
        String platform = pAPI[0];
        LOGGER.info("Building language: " + mLang + " for system: " + mSystem);

        String lang_path="";
        try {
            lang_path = RavelProperties.getInstance().getInstance().get_language_package() + mLang;
            mLanguage = (ConcreteLanguage) Class.forName(lang_path).newInstance();

        } catch(ExceptionInInitializerError e){
            System.err.println("Could not find " + lang_path);
        }

        String platform_path = "";
        try {
            platform_path = RavelProperties.getInstance().get_platform_package() + platform;
            mPlatform = (ConcretePlatform) Class.forName(platform_path).newInstance();
        } catch ( ExceptionInInitializerError e){
            System.err.println("Could not find " + lang_path);
        }

        if (!mPlatform.allowsLanguage(mLanguage)) {
            // FIXME emit a better error
            throw new IllegalArgumentException("Platform " + mPlatform.getClass().getSimpleName() + " is not compatible with language " + mLanguage.getClass().getSimpleName());
        }

        if (pAPI.length > 1) {
            mPlatform.setAPILevel(Integer.valueOf(pAPI[1].substring(1)));

        }

        //TODO: resolve existence of the references to sinks and sources
    }

    public static class Builder {
        private String system;
        private String lang;
        private String name;

        public Platform build(){
            return new Platform(system, lang);
        }

        public Builder name(String name) {
            // FIXME
            this.name = name;
            return this;
        }

        public Builder language(String lang) {
            this.lang = lang;
            return this;
        }
        public Builder system(String sys) {
            this.system = sys;
            return this;
        }
    }
}
