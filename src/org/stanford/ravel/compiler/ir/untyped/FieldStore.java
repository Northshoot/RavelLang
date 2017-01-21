package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.stanford.ravel.compiler.ir.untyped.UntypedIR.VOID_REG;

/**
 * Created by gcampagn on 1/20/17.
 */
public class FieldStore extends Instruction {
    private final int object;
    private final int value;
    private final String field;

    public FieldStore(ParserRuleContext definer, int object, String field, int value) {
        super(definer);

        this.object = object;
        this.field = field;
        this.value = value;
    }

    public String toString() {
        return "field.st " + object + " ." + field + " = " + value;
    }
}
