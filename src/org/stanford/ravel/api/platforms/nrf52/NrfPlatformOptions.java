package org.stanford.ravel.api.platforms.nrf52;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.platforms.PlatformOptions;

/**
 * Created by gcampagn on 2/6/17.
 */
public class NrfPlatformOptions extends PlatformOptions {
    private static NrfPlatformOptions instance = new NrfPlatformOptions();

    private NrfPlatformOptions() {
        //TODO: make dynamic loading from the app config file
        super( "runtime/nrf52",  "/Users/lauril/workspace/12-sdk/nRF5_SDK_12.0.0/");
    }

    public static NrfPlatformOptions getInstance() {
        return instance;
    }






    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (prefix.equals("nrf52")) {
            switch (option) {
                case "runtime-path":
                    runtimePath = value;
                    break;
                case "path":
                    path = value;
                    break;

                default:
                    throw new InvalidOptionException("Unrecognized option -Xnrf:" + option);
            }
        }

        return super.setOption(prefix, option, value);
    }
}
