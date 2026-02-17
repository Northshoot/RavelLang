package ai.harmony.ravel.api.lang;

import ai.harmony.ravel.compiler.ir.typed.TypedIR;

/**
 * Translate IR to target code
 *
 * Created by gcampagn on 1/24/17.
 */
public interface IRTranslator {
    void translate(TypedIR ir);

    String getCode();
}
