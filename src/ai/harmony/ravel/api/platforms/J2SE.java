package ai.harmony.ravel.api.platforms;

import ai.harmony.ravel.RavelProperties;
import ai.harmony.ravel.api.builder.CodeModule;
import ai.harmony.ravel.api.builder.FileObject;
import ai.harmony.ravel.api.lang.ConcreteLanguage;
import ai.harmony.ravel.api.lang.JLang;
import ai.harmony.ravel.api.lang.java.JavaLanguageOptions;
import ai.harmony.ravel.api.platforms.contiki.ContikiPlatformOptions;
import ai.harmony.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Standard desktop Java
 *
 * Created by gcampagn on 1/31/17.
 */
public class J2SE extends BasePlatform {
    private final static String BASE_LANG_TMPL_PATH = RavelProperties.getInstance().get_j2se_tmpl_dir();

    private final STGroup mainGroup;
    private final STGroup buildGroup;
    private final String mMainFileName = "Application";

    public J2SE() {
        // java 1.8 required
        super(18, Integer.MAX_VALUE);
        mainGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg");
        buildGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/build.stg");
    }

    @Override
    protected CodeModule createLauncher(Space s) {
        JavaLanguageOptions javaOptions = JavaLanguageOptions.getInstance();

        ST tmpl = mainGroup.getInstanceOf("file");
        tmpl.add("package", javaOptions.getPackageName());

        FileObject file = new FileObject();
        file.setFileName(mMainFileName+ ".java");
        file.setContent(tmpl.render());
        file.setSubPath("src/" + javaOptions.getPackageName().replace(".", "/"));
        CodeModule module = new CodeModule();
        module.addFile(file);

        return module;
    }

    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        JavaLanguageOptions joptions = JavaLanguageOptions.getInstance();
        String runtimePath = new File(joptions.getRuntimePath()).getAbsolutePath();

        ST tmpl = buildGroup.getInstanceOf("ant");
        tmpl.add("java_runtime", runtimePath);
        tmpl.add("main_class", joptions.getPackageName()+"."+mMainFileName);
        tmpl.add("java_jarlib_path", RavelProperties.getInstance().getLibJarPath());
        FileObject build_xml = new FileObject();
        build_xml.setFileName("build.xml");
        build_xml.setContent(tmpl.render());
        return Collections.singletonList(build_xml);
    }

    @Override
    public PlatformOptions getOptions() {
        return ContikiPlatformOptions.getInstance();
    }

    @Override
    public boolean allowsLanguage(ConcreteLanguage lang) {
        return lang instanceof JLang;
    }
}
