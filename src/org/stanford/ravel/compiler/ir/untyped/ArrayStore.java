package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Arrays;
import java.util.List;

import static org.stanford.ravel.compiler.ir.untyped.UntypedIR.VOID_REG;

/**
 * Created by gcampagn on 1/20/17.
 */
public class ArrayStore extends Instruction {
    private final int object;
    private final int value;
    private final int index;

    public ArrayStore(ParserRuleContext definer, int object, int index, int value) {
        super(definer);

        this.object = object;
        this.index = index;
        this.value = value;
    }

    public String toString() {
        return "field.st " + object + " [ " + index + " ] = " + value;
    }
}
