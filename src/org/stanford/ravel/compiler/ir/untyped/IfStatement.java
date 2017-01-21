package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;

import static org.stanford.ravel.compiler.ir.untyped.UntypedIR.VOID_REG;

/**
 * Created by gcampagn on 1/20/17.
 */
public class IfStatement extends Instruction {
    private final int cond;
    private final Block iftrue;
    private final Block iffalse;

    public IfStatement(ParserRuleContext definer, int cond, Block iftrue, Block iffalse) {
        super(definer);

        this.cond = cond;
        this.iftrue = iftrue;
        this.iffalse = iffalse;
    }

    public String toString() {
        return "if " + cond + " {\n" + iftrue + "} else {\n" + iffalse + "}";
    }

    void accept(InstructionVisitor visitor) {
        super.accept(visitor);
        iftrue.accept(visitor);
        iffalse.accept(visitor);
    }
}
