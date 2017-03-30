package edu.stanford.ravel.compiler.ir.typed;

/**
 * Created by gcampagn on 1/23/17.
 */
public interface ControlFlowGraphVisitor {
    void visitBlock(TBlock block);
}
