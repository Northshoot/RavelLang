package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.CLang;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.lang.c.CLanguageOptions;
import org.stanford.ravel.api.platforms.posix.PosixRuntimeOptions;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * Standard C/POSIX OS
 *
 * Created by gcampagn on 1/31/17.
 */
public class Posix extends BaseCPlatform {
    private final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/platforms/posix/tmpl";


    public Posix() {
        // POSIX.1-2008/SUSv4 minimum required
        super(
                2008, Integer.MAX_VALUE,
                new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg"),
                new STGroupFile(BASE_LANG_TMPL_PATH + "/makefile.stg")
        );
    }

    @Override
    public PlatformOptions getOptions() {
        return PosixRuntimeOptions.getInstance();
    }

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof CLang;
    }



    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        return super.createBuildSystem(s, files, PosixRuntimeOptions.getInstance());
    }
}
