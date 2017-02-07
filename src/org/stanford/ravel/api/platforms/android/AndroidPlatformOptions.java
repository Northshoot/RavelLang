package org.stanford.ravel.api.platforms.android;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.platforms.PlatformOptions;
import org.stanford.ravel.api.platforms.contiki.ContikiPlatformOptions;

/**
 * Created by gcampagn on 2/6/17.
 */
public class AndroidPlatformOptions extends PlatformOptions {
    private static AndroidPlatformOptions instance = new AndroidPlatformOptions();

    private AndroidPlatformOptions() {}

    public static AndroidPlatformOptions getInstance() {
        return instance;
    }

    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (prefix.equals("android")) {
            switch (option) {
                default:
                    throw new InvalidOptionException("Unrecognized option -Xandroid:" + option);
            }
        }

        return super.setOption(prefix, option, value);
    }
}

