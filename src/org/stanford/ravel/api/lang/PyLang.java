package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.primitives.*;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class PyLang extends BaseLanguage {
    public final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/lang/python/tmpl";

    public PyLang() {
    }

    @Override
    public OptionParser getOptions() {
        return new LanguageOptions();
    }

    @Override
    protected CodeModule createDispatcher(Space s) {
        // TODO
        return null;
    }

    @Override
    protected CodeModule createModel(ConcreteModel im) {
        // TODO
        return new CodeModule();
    }

    @Override
    protected CodeModule createController(ConcreteController ictr) {
        // TODO
        return new CodeModule();
    }

    @Override
    protected CodeModule createView(ConcreteView iview) {
        // TODO
        return new CodeModule();
    }

    @Override
    protected CodeModule createInterface(ConcreteInterface iiface) {
        // TODO
        return new CodeModule();
    }
}
