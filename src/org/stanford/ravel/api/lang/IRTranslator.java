package org.stanford.ravel.api.lang;

import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;

import java.util.List;

/**
 * Translate IR to target code
 *
 * Created by gcampagn on 1/24/17.
 */
public interface IRTranslator {
    void declareRegister(int reg, Type type);

    void translate(List<VariableSymbol> controllerParams, List<VariableSymbol> eventParams, TypedIR ir);

    String getCode();
}
