package ai.harmony.ravel.api.lang;

import ai.harmony.ravel.RavelProperties;
import ai.harmony.ravel.api.OptionParser;
import ai.harmony.ravel.api.builder.CodeModule;
import ai.harmony.ravel.primitives.*;

import java.util.logging.Logger;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class PyLang extends BaseLanguage {
    private static Logger LOGGER = Logger.getLogger(CLang.class.getName());
    private final static String app_dir = RavelProperties.getInstance().get_fileOutDir();
    public final static String BASE_LANG_TMPL_PATH = RavelProperties.getInstance().get_python_tmpl_dir();

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
