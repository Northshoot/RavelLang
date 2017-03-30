package edu.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 1/20/17.
 */
public class IfStatement extends Instruction {
    public final int cond;
    public final Block iftrue;
    public final Block iffalse;

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
        visitor.visit(this);
    }
}
