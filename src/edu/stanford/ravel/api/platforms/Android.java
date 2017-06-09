package edu.stanford.ravel.api.platforms;

import edu.stanford.ravel.RavelProperties;
import edu.stanford.ravel.api.Settings;
import edu.stanford.ravel.api.builder.CodeModule;
import edu.stanford.ravel.api.builder.FileObject;
import edu.stanford.ravel.api.lang.java.JavaLanguageOptions;
import edu.stanford.ravel.api.platforms.android.AndroidPlatformOptions;
import edu.stanford.ravel.primitives.Space;
import edu.stanford.ravel.api.lang.ConcreteLanguage;
import edu.stanford.ravel.api.lang.JLang;
import edu.stanford.ravel.api.platforms.contiki.ContikiPlatformOptions;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by lauril on 9/7/16.
 */
public class Android  extends BasePlatform {
    private final static String BASE_LANG_TMPL_PATH = RavelProperties.getInstance().get_android_tmpl_dir();

    private static Logger LOGGER = Logger.getLogger(Android.class.getName());

    private final STGroup mainGroup;
    private final STGroup buildGroup;

    public Android() {
        // API level 19 (4.4.3) minimum required, because we need at least Java 1.7
        super(19, Integer.MAX_VALUE);

        mainGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg");
        buildGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/build.stg");
    }

    @Override
    public PlatformOptions getOptions() {
        return ContikiPlatformOptions.getInstance();
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
        tmpl.add("name", s.getName());

        FileObject file = new FileObject();
        file.setFileName(s.getName() + ".java");
        file.setContent(tmpl.render());
        file.setSubPath("src/" + javaOptions.getPackageName().replace(".", "/"));
        CodeModule module = new CodeModule();
        module.addFile(file);

        return module;
    }

    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        JavaLanguageOptions joptions = JavaLanguageOptions.getInstance();
        List<String> jfiles = new ArrayList<>();

        for (FileObject file : files) {
            String fileName = file.getFileName();
            if (fileName.endsWith(".java"))
                file.setSubPath(file.getSubPath().replace("src/", "src/main/java/"));
        }

        String runtimePath = new File(joptions.getRuntimePath()).getAbsolutePath();

        AndroidPlatformOptions androidoptions = AndroidPlatformOptions.getInstance();
        String androidRuntimePath = new File(androidoptions.getRuntimePath()).getAbsolutePath();

        ST gradle_tmpl = buildGroup.getInstanceOf("gradle");
        gradle_tmpl.add("sdk_version", getAPILevel());
        gradle_tmpl.add("java_runtime", runtimePath);
        gradle_tmpl.add("android_runtime", androidRuntimePath);

        FileObject gradle = new FileObject();
        gradle.setFileName("build.gradle");
        gradle.setContent(gradle_tmpl.render());

        ST manifest_tmpl = buildGroup.getInstanceOf("manifest");
        manifest_tmpl.add("name", s.getName());
        manifest_tmpl.add("package", joptions.getPackageName());

        FileObject manifest = new FileObject();
        manifest.setFileName("AndroidManifest.xml");
        manifest.setSubPath("src/main");
        manifest.setContent(manifest_tmpl.render());

        ST runtime_ref_tmpl = buildGroup.getInstanceOf("gradle_runtime_ref");
        runtime_ref_tmpl.add("android_runtime", androidRuntimePath);

        FileObject runtimeRef = new FileObject();
        runtimeRef.setFileName("build.gradle");
        runtimeRef.setSubPath("runtime");
        runtimeRef.setContent(runtime_ref_tmpl.render());

        ST settings_tmpl = buildGroup.getInstanceOf("gradle_settings");

        FileObject settings = new FileObject();
        settings.setFileName("settings.gradle");
        settings.setContent(settings_tmpl.render());

        return Arrays.asList(gradle, manifest, runtimeRef, settings);
    }
}
