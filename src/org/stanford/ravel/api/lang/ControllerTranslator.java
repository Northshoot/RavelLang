package org.stanford.ravel.api.lang;

import org.stanford.ravel.primitives.Controller;

/**
 * Translate a controller into a module
 *
 * Created by gcampagn on 1/27/17.
 */
public interface ControllerTranslator {
    CodeModule translate(Controller ctr);
}
