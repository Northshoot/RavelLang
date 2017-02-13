package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
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

/**
 * The Contiki Embedded OS
 *
 * Created by gcampagn on 2/3/17.
 */
public class BaseCPlatform extends BasePlatform {

    protected final STGroup mainGroup;
    protected final STGroup makefileGroup;

    public BaseCPlatform( STGroupFile mainGroup, STGroupFile makefileGroup ) {
        super();
        this.mainGroup = mainGroup;
        this.makefileGroup = makefileGroup;
    }

    public BaseCPlatform(int i, int maxValue, STGroupFile mainGroup, STGroupFile makefileGroup) {
        super(i, maxValue);
        this.mainGroup = mainGroup;
        this.makefileGroup = makefileGroup;
    }

    @Override
    public PlatformOptions getOptions() {
        return ContikiPlatformOptions.getInstance();
    }

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof CLang;
    }

    public List<String> getFileList(Space s, List<FileObject> files){
        List<String> cfiles = new ArrayList<>();
        for (FileObject file : files) {
            String fileName = file.getFileName();
            if (fileName.equals(s.getName() + ".c"))
                continue;
            if (fileName.endsWith(".c"))
                cfiles.add(fileName.substring(0, fileName.length()-2));
        }
        return cfiles;
    }

    public List<FileObject> createBuildSystem(Space s, List<FileObject> files, PlatformOptions platformOptions) {
        List<String> cfiles = getFileList(s,files);



        CLanguageOptions coptions = CLanguageOptions.getInstance();
        String runtimePath = new File(coptions.getRuntimePath()).getAbsolutePath();

        String path = new File(platformOptions.getPlatformPath()).getAbsolutePath();
        String platformRuntimePath = new File(platformOptions.getRuntimePath()).getAbsolutePath();

        ST tmpl = makefileGroup.getInstanceOf("application");
        tmpl.add("target", s.getName());
        tmpl.add("sources", cfiles);
        tmpl.add("runtime", runtimePath);
        tmpl.add("plat_runtime", platformRuntimePath);
        tmpl.add("path", path);

        FileObject file = new FileObject();
        file.setFileName("Makefile");
        file.setContent(tmpl.render());
        return Collections.singletonList(file);
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

}
