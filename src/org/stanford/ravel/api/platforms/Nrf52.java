package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.CLang;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.platforms.nrf52.NrfPlatformOptions;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * The NRF52 runtime
 *
 * Created by lauril on 2/13/17.
 */
public class Nrf52 extends BaseCPlatform {
    private final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/platforms/nrf52/tmpl";

    public Nrf52() {
        super(
                new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg"),
                new STGroupFile(BASE_LANG_TMPL_PATH + "/makefile.stg")
        );
    }
    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        NrfPlatformOptions platoptions = NrfPlatformOptions.getInstance();
        return super.createBuildSystem(s, files, platoptions);
    }
    @Override
    public PlatformOptions getOptions() {
        return NrfPlatformOptions.getInstance();
    }

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof CLang;
    }

}
