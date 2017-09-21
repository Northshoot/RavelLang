package ai.harmony.ravel.api.platforms.android;

import ai.harmony.ravel.api.InvalidOptionException;
import ai.harmony.ravel.api.platforms.PlatformOptions;

/**
 * Created by gcampagn on 2/6/17.
 */
public class AndroidPlatformOptions extends PlatformOptions {
    private static AndroidPlatformOptions instance = new AndroidPlatformOptions();

    private AndroidPlatformOptions() {
        super("./runtime/android/build/outputs/aar/android-debug.aar", "");
    }

    public static AndroidPlatformOptions getInstance() {
        return instance;
    }


    public String getRuntimePath() {
        return runtimePath;
    }

    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (prefix.equals("android")) {
            switch (option) {
                case "runtime-path":
                    runtimePath = value;
                    break;
                default:
                    throw new InvalidOptionException("Unrecognized option -Xandroid:" + option);
            }
        }

        return super.setOption(prefix, option, value);
    }
}

