package edu.stanford.ravel.api.lang;

import edu.stanford.ravel.compiler.ir.typed.TypedIR;

/**
 * Translate IR to target code
 *
 * Created by gcampagn on 1/24/17.
 */
public interface IRTranslator {
    void translate(TypedIR ir);

    String getCode();
}
