package org.stanford.ravel.primitives;

import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.platforms.BasePlatform;

import java.util.logging.Logger;

/**
 * Created by lauril on 9/7/16.
 */
public class Platform {
    private static Logger LOGGER = Logger.getLogger(Platform.class.getName());

    private final String mSystem;
    private final String mLang;

    private BasePlatform mPlatform;
    private ConcreteLanguage mLanguage;

    //we instantiate a real platform that will provide API

    private Platform(String system, String lang) {
        this.mSystem = system;
        this.mLang = lang;
    }

    public BasePlatform getConcretePlatform() {
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

        mLanguage = (ConcreteLanguage) Class.forName("org.stanford.ravel.api.lang." + mLang).newInstance();
        mPlatform = (BasePlatform) Class.forName("org.stanford.ravel.api.platforms." + platform).newInstance();
        if (pAPI.length > 1)
            mPlatform.setAPILevel(pAPI[1]);

        //TODO: resolve existence of the references to sinks and sources
    }

    public static class Builder {
        private String system;
        private String lang;

        public Platform build(){
            return new Platform(system, lang);
        }

        public Builder name(String name) {
            // FIXME
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
