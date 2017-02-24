package org.stanford.ravel.api;

/**
 * Created by gcampagn on 1/29/17.
 */
public interface OptionParser {
    boolean setOption(String prefix, String option, String value) throws InvalidOptionException;
}
