package edu.stanford.ravel.api.platforms;

import edu.stanford.ravel.RavelProperties;
import edu.stanford.ravel.api.Settings;
import edu.stanford.ravel.api.builder.FileObject;
import edu.stanford.ravel.primitives.Space;
import edu.stanford.ravel.api.platforms.contiki.ContikiPlatformOptions;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

/**
 * The Contiki Embedded OS
 *
 * Created by gcampagn on 2/3/17.
 */
public class Contiki extends BaseCPlatform {
    private final static String BASE_LANG_TMPL_PATH = RavelProperties.get_contiki_tmpl_dir();



    public Contiki() {
        super(
                new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg"),
                new STGroupFile(BASE_LANG_TMPL_PATH + "/makefile.stg")
                );
    }


    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        return super.createBuildSystem(s, files, ContikiPlatformOptions.getInstance(), null);
    }

    @Override
    public PlatformOptions getOptions() {
        return ContikiPlatformOptions.getInstance();
    }


}
