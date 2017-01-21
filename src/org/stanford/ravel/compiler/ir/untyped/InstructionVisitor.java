package org.stanford.ravel.compiler.ir.untyped;

/**
 * Created by gcampagn on 1/20/17.
 */
public interface InstructionVisitor {
    default void beginBlock(Block block) {
    }

    default void endBlock(Block block) {
    }

    void visit(Instruction instr);
}
