package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.OptionParser;

/**
 * Created by gcampagn on 1/31/17.
 */
public class PlatformOptions implements OptionParser {
    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (!prefix.equals("platform"))
            return false;

        throw new InvalidOptionException("Unrecognized option -Xplatform:" + option);
    }
}
