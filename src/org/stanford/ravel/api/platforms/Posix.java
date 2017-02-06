package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.CLang;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.lang.JLang;
import org.stanford.ravel.api.lang.c.CLanguageOptions;
import org.stanford.ravel.api.lang.java.JavaLanguageOptions;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof CLang;
    }

    @Override
    protected CodeModule createLauncher(Space s) {
        ST tmpl = mainGroup.getInstanceOf("file");

        FileObject file = new FileObject();
        file.setFileName("main.c");
        file.setContent(tmpl.render());
        CodeModule module = new CodeModule();
        module.addFile(file);

        return module;
    }

    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        List<String> cfiles = new ArrayList<>();

        for (FileObject file : files) {
            String fileName = file.getRelativeName();
            if (fileName.endsWith(".c"))
                cfiles.add(fileName.substring(0, fileName.length()-2));
        }

        CLanguageOptions options = CLanguageOptions.getInstance();
        String runtimePath = new File(options.getRuntimePath()).getAbsolutePath();

        ST tmpl = makefileGroup.getInstanceOf("application");
        tmpl.add("target", s.getName());
        tmpl.add("sources", cfiles);
        tmpl.add("runtime", runtimePath);

        FileObject file = new FileObject();
        file.setFileName("Makefile");
        file.setContent(tmpl.render());
        return Collections.singletonList(file);
    }
}
