package ai.harmony.ravel.api.lang;

import ai.harmony.ravel.api.InvalidOptionException;
import ai.harmony.ravel.api.OptionParser;

/**
 * Created by gcampagn on 1/29/17.
 */
public class LanguageOptions implements OptionParser {
    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (!prefix.equals("lang"))
            return false;

        throw new InvalidOptionException("Unrecognized option -Xlang:" + option);
    }
}
