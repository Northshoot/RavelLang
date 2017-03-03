package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.ir.Registers;

/**
 * Created by gcampagn on 1/20/17.
 */
public class MethodCall extends Instruction {
    public final int owner;
    public final String method;
    public final int[] arguments;
    public final int target;

    public MethodCall(ParserRuleContext definer, int target, int owner, String method, int[] arguments) {
        super(definer);
        this.target = target;
        this.owner = owner;
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        String str = "call " + target + " = " + (owner != Registers.VOID_REG ? owner + " ." : "") + method + " (";
        for (int arg : arguments) {
            str += " " + arg + ", ";
        }
        return str + ")";
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
