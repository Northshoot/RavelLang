package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.OptionParser;

/**
 * Created by gcampagn on 1/31/17.
 */
public class PlatformOptions implements OptionParser {
    private boolean debug = false;

    public boolean getDebug() { return this.debug; }

    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (!prefix.equals("platform"))
            return false;
        switch (option){
            case "debug":
                debug = Boolean.valueOf(value);
        }
        throw new InvalidOptionException("Unrecognized option -Xplatform:" + option);
    }
}
