package edu.stanford.ravel.api.platforms.posix;

import edu.stanford.ravel.api.InvalidOptionException;
import edu.stanford.ravel.api.platforms.PlatformOptions;

/**
 * Created by gcampagn on 2/6/17.
 */
public class PosixRuntimeOptions extends PlatformOptions {
    private static PosixRuntimeOptions instance = new PosixRuntimeOptions();

    private PosixRuntimeOptions() {
        super("./runtime/posix", "");
    }

    public static PosixRuntimeOptions getInstance() {
        return instance;
    }


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
