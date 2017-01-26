package org.stanford.ravel.compiler.backend;

import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.types.Type;

/**
 * Translate IR to target code
 *
 * Created by gcampagn on 1/24/17.
 */
public interface Translator {
    void declareParameter(String name, int reg, Type type);
    void declareRegister(int reg, Type type);

    void translate(TypedIR ir);

    String getCode();
}
