package edu.stanford.ravel.api.lang;

import edu.stanford.ravel.api.OptionParser;
import edu.stanford.ravel.api.Settings;
import edu.stanford.ravel.api.builder.CodeModule;
import edu.stanford.ravel.primitives.*;

import java.util.logging.Logger;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class PyLang extends BaseLanguage {
    private static Logger LOGGER = Logger.getLogger(CLang.class.getName());
    private final static String app_dir = "app_files/";
    public final static String BASE_LANG_TMPL_PATH = Settings.BASE_TMPL_PATH +"/lang/python/tmpl";

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
