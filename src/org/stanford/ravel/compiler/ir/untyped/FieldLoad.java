package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class FieldLoad extends Instruction {
    private final int target;
    private final int source;
    private final String field;

    public FieldLoad(ParserRuleContext definer, int target, int source, String field) {
        super(definer);

        this.target = target;
        this.source = source;
        this.field = field;
    }

    public String toString() {
        return "field.ld " + target + " = " + source + " ." + field;
    }
}
