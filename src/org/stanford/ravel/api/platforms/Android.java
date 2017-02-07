package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.lang.JLang;
import org.stanford.ravel.api.lang.java.JavaLanguageOptions;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;
import java.util.logging.Logger;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * Created by lauril on 9/7/16.
 */
public class Android  extends BasePlatform {
    private final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/platforms/j2se/tmpl";

    private static Logger LOGGER = Logger.getLogger(Android.class.getName());

    private final STGroup mainGroup;

    public Android() {
        // API level 19 (4.4.3) minimum required, because we need at least Java 1.7
        super(19, Integer.MAX_VALUE);

        mainGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg");
    }

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof JLang;
    }

    @Override
    protected CodeModule createLauncher(Space s) {
        JavaLanguageOptions javaOptions = JavaLanguageOptions.getInstance();

        ST tmpl = mainGroup.getInstanceOf("file");
        tmpl.add("package", javaOptions.getPackageName());

        FileObject file = new FileObject();
        file.setFileName("Application.java");
        file.setContent(tmpl.render());
        file.setSubPath("src/" + javaOptions.getPackageName().replace(".", "/"));
        CodeModule module = new CodeModule();
        module.addFile(file);

        return module;
    }
}
