package org.stanford.ravel.api.lang.java;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.lang.LanguageOptions;

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

    private String packageName = "org.stanford.ravel.generated";

    public String getPackageName() {
        return packageName;
    }

    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (prefix.equals("java")) {
            switch (option) {
                case "package":
                    packageName = value;
                    break;
                default:
                    throw new InvalidOptionException("Unrecognized option -Xjava:" + option);
            }
        }

        return super.setOption(prefix, option, value);
    }
}
