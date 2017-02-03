package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.BaseLanguage;
import org.stanford.ravel.api.lang.CLang;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * The Contiki Embedded OS
 *
 * Created by gcampagn on 2/3/17.
 */
public class Contiki extends BasePlatform {
    private final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/platforms/contiki/tmpl";

    private final STGroup mainGroup;
    private final STGroup makefileGroup;

    public Contiki() {
        mainGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg");
        makefileGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/makefile.stg");
    }

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof CLang;
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
