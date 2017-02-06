package org.stanford.ravel.api.platforms.contiki;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.lang.java.JavaLanguageOptions;
import org.stanford.ravel.api.platforms.PlatformOptions;

/**
 * Created by gcampagn on 2/6/17.
 */
public class ContikiPlatformOptions extends PlatformOptions {
    private static ContikiPlatformOptions instance = new ContikiPlatformOptions();

    private ContikiPlatformOptions() {}

    public static ContikiPlatformOptions getInstance() {
        return instance;
    }

    private String runtimePath = "./runtime/contiki";
    private String path = "../contiki";

    public String getContikiPath() {
        return path;
    }

    public String getRuntimePath() {
        return runtimePath;
    }

    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (prefix.equals("contiki")) {
            switch (option) {
                case "runtime-path":
                    runtimePath = value;
                    break;
                case "path":
                    path = value;
                    break;
                default:
                    throw new InvalidOptionException("Unrecognized option -Xcontiki:" + option);
            }
        }

        return super.setOption(prefix, option, value);
    }
}
