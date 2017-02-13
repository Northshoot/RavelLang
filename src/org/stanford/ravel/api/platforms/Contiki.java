package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.CLang;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.platforms.contiki.ContikiPlatformOptions;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * The Contiki Embedded OS
 *
 * Created by gcampagn on 2/3/17.
 */
public class Contiki extends BaseCPlatform {
    private final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/platforms/contiki/tmpl";



    public Contiki() {
        super(
                new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg"),
                new STGroupFile(BASE_LANG_TMPL_PATH + "/makefile.stg")
                );
    }


    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        ContikiPlatformOptions platoptions = ContikiPlatformOptions.getInstance();
        return super.createBuildSystem(s, files, platoptions);
    }

    @Override
    public PlatformOptions getOptions() {
        return ContikiPlatformOptions.getInstance();
    }

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof CLang;
    }

}
