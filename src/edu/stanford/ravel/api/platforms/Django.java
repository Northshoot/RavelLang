package edu.stanford.ravel.api.platforms;

import edu.stanford.ravel.api.lang.PyLang;
import edu.stanford.ravel.api.lang.ConcreteLanguage;
import edu.stanford.ravel.api.platforms.contiki.ContikiPlatformOptions;

import java.util.logging.Logger;

/**
 * Created by lauril on 9/7/16.
 */
public class Django extends BasePlatform {
    private static Logger LOGGER = Logger.getLogger(Django.class.getName());

    @Override
    public PlatformOptions getOptions() {
        return ContikiPlatformOptions.getInstance();
    }

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof PyLang;
    }
}
