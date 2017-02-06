package org.stanford.ravel.api.platforms.posix;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.platforms.PlatformOptions;
import org.stanford.ravel.api.platforms.contiki.ContikiPlatformOptions;

/**
 * Created by gcampagn on 2/6/17.
 */
public class PosixRuntimeOptions extends PlatformOptions {
    private static PosixRuntimeOptions instance = new PosixRuntimeOptions();

    private PosixRuntimeOptions() {}

    public static PosixRuntimeOptions getInstance() {
        return instance;
    }

    private String runtimePath = "./runtime/posix";

    public String getRuntimePath() {
        return runtimePath;
    }

    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (prefix.equals("posix")) {
            switch (option) {
                case "runtime-path":
                    runtimePath = value;
                    break;
                default:
                    throw new InvalidOptionException("Unrecognized option -Xposix:" + option);
            }
        }

        return super.setOption(prefix, option, value);
    }
}
