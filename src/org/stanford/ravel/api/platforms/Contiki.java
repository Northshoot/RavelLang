package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.BaseLanguage;
import org.stanford.ravel.api.lang.CLang;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.lang.c.CLanguageOptions;
import org.stanford.ravel.api.platforms.contiki.ContikiPlatformOptions;
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
    public OptionParser getOptions() {
        return ContikiPlatformOptions.getInstance();
    }

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof CLang;
    }

    @Override
    protected CodeModule createLauncher(Space s) {
        ST tmpl = mainGroup.getInstanceOf("file");
        tmpl.add("name", s.getName());

        FileObject file = new FileObject();
        file.setFileName(s.getName() + ".c");
        file.setContent(tmpl.render());
        CodeModule module = new CodeModule();
        module.addFile(file);

        return module;
    }

    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        List<String> cfiles = new ArrayList<>();

        for (FileObject file : files) {
            String fileName = file.getFileName();
            if (fileName.equals(s.getName() + ".c"))
                continue;
            if (fileName.endsWith(".c"))
                cfiles.add(fileName.substring(0, fileName.length()-2));
        }

        CLanguageOptions coptions = CLanguageOptions.getInstance();
        String runtimePath = new File(coptions.getRuntimePath()).getAbsolutePath();

        ContikiPlatformOptions platoptions = ContikiPlatformOptions.getInstance();
        String contikiPath = new File(platoptions.getContikiPath()).getAbsolutePath();
        String platformRuntimePath = new File(platoptions.getRuntimePath()).getAbsolutePath();

        ST tmpl = makefileGroup.getInstanceOf("application");
        tmpl.add("target", s.getName());
        tmpl.add("sources", cfiles);
        tmpl.add("c_runtime", runtimePath);
        tmpl.add("plat_runtime", platformRuntimePath);
        tmpl.add("contiki", contikiPath);

        FileObject file = new FileObject();
        file.setFileName("Makefile");
        file.setContent(tmpl.render());
        return Collections.singletonList(file);
    }
}
