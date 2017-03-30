package edu.stanford.ravel.api.platforms;

import edu.stanford.ravel.api.Settings;
import edu.stanford.ravel.api.platforms.posix.PosixRuntimeOptions;
import edu.stanford.ravel.primitives.Space;
import edu.stanford.ravel.api.builder.FileObject;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

/**
 * Standard C/POSIX OS
 *
 * Created by gcampagn on 1/31/17.
 */
public class Posix extends BaseCPlatform {
    private final static String BASE_LANG_TMPL_PATH = Settings.BASE_TMPL_PATH +"/platforms/posix/tmpl";


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
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        return super.createBuildSystem(s, files, PosixRuntimeOptions.getInstance(), null);
    }
}
