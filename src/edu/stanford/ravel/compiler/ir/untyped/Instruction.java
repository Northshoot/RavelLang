package edu.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by gcampagn on 1/20/17.
 */
public abstract class Instruction {
    public final ParserRuleContext definer;

    public Instruction(ParserRuleContext definer) {
        this.definer = definer;
    }

    abstract void accept(InstructionVisitor visitor);
}
