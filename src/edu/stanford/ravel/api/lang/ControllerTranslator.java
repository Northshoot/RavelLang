package edu.stanford.ravel.api.lang;

import edu.stanford.ravel.api.builder.CodeModule;
import edu.stanford.ravel.primitives.Controller;

/**
 * Translate a controller into a module
 *
 * Created by gcampagn on 1/27/17.
 */
public interface ControllerTranslator {
    CodeModule translate(Controller ctr);
}
