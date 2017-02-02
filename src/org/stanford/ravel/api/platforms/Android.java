package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.lang.JLang;
import org.stanford.ravel.primitives.Space;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by lauril on 9/7/16.
 */
public class Android  extends BasePlatform {
    private static Logger LOGGER = Logger.getLogger(Android.class.getName());

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof JLang;
    }
    // TODO
}
