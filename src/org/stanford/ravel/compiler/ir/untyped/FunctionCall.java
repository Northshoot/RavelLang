package org.stanford.ravel.compiler.ir.untyped;

import org.stanford.antlr4.RavelParser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class FunctionCall extends Instruction {
    private final int callee;
    private final int[] arguments;
    private final int target;

    public FunctionCall(RavelParser.Function_callContext definer, int target, int callee, int[] arguments) {
        super(definer);
        this.target = target;
        this.callee = callee;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        String str = "call " + target + " = " + callee + " (";
        for (int arg : arguments) {
            str += " " + arg + ", ";
        }
        return str + ")";
    }
}
