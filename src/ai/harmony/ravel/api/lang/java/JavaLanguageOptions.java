package ai.harmony.ravel.api.lang.java;

import ai.harmony.ravel.api.InvalidOptionException;
import ai.harmony.ravel.api.lang.LanguageOptions;

/**
 * Holds command line options specific to Java, such as package name
 *
 * Created by gcampagn on 1/29/17.
 */
public class JavaLanguageOptions extends LanguageOptions {
    private static JavaLanguageOptions instance = new JavaLanguageOptions();

    private JavaLanguageOptions() {}

    public static JavaLanguageOptions getInstance() {
        return instance;
    }

    //TODO: move to preferences
    private String packageName = "org.stanford.ravel.generated";
    private String runtimePath = "./runtime/java/build/libs/java-0.9.jar";

    public String getPackageName() {
        return packageName;
    }

    public String getRuntimePath() {
        return runtimePath;
    }

    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (prefix.equals("java")) {
            switch (option) {
                case "package":
                    packageName = value;
                    break;
                case "runtime-path":
                    runtimePath = value;
                    break;
                default:
                    throw new InvalidOptionException("Unrecognized option -Xjava:" + option);
            }
        }

        return super.setOption(prefix, option, value);
    }
}
