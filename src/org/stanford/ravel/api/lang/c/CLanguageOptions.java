package org.stanford.ravel.api.lang.c;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.lang.LanguageOptions;
import org.stanford.ravel.api.lang.java.JavaLanguageOptions;

/**
 * Created by gcampagn on 1/31/17.
 */
public class CLanguageOptions extends LanguageOptions {
    private static CLanguageOptions instance = new CLanguageOptions();

    private CLanguageOptions() {}

    public static CLanguageOptions getInstance() {
        return instance;
    }

    private String runtimePath = "./runtime/c";

    public String getRuntimePath() {
        return runtimePath;
    }

    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (prefix.equals("c")) {
            switch (option) {
                case "runtime-path":
                    runtimePath = value;
                    break;
                default:
                    throw new InvalidOptionException("Unrecognized option -Xc:" + option);
            }
        }

        return super.setOption(prefix, option, value);
    }
}