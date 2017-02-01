package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.java.JavaLanguageOptions;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * Standard C/POSIX OS
 *
 * Created by gcampagn on 1/31/17.
 */
public class Posix extends BasePlatform {
    private final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/platforms/posix/tmpl";

    private final STGroup mainGroup;
    private final STGroup makefileGroup;

    public Posix() {
        mainGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg");
        makefileGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/makefile.stg");
    }

    @Override
    protected CodeModule createLauncher() {
        ST tmpl = mainGroup.getInstanceOf("file");

        FileObject file = new FileObject();
        file.setFileName("main.c");
        file.setContent(tmpl.render());
        CodeModule module = new CodeModule();
        module.addFile(file);

        return module;
    }

    @Override
    protected CodeModule createBuildSystem(Space s, List<FileObject> files) {
        ST tmpl = makefileGroup.getInstanceOf("application");
        tmpl.add("target", s.getName());

        FileObject file = new FileObject();
        file.setFileName("Makefile");
        file.setContent(tmpl.render());
        CodeModule module = new CodeModule();
        module.addFile(file);

        return module;
    }
}
