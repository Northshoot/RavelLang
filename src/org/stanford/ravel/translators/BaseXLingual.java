package org.stanford.ravel.translators;

import org.stanford.ravel.error.exceptions.TranslationError;

/**
 * Created by lauril on 1/10/17.
 */
public interface BaseXLingual {

    String getcName() throws TranslationError;

    String getpName() throws TranslationError;

    String getjName() throws TranslationError;
}
