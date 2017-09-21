package ai.harmony.ravel.api.lang;

import ai.harmony.ravel.api.builder.CodeModule;
import ai.harmony.ravel.primitives.Controller;

/**
 * Translate a controller into a module
 *
 * Created by gcampagn on 1/27/17.
 */
public interface ControllerTranslator {
    CodeModule translate(Controller ctr);
}
