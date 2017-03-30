package edu.stanford.ravel.compiler.ir.typed;

/**
 * Created by gcampagn on 1/25/17.
 */
public interface LoopTreeVisitor {
    void visit(LoopTreeNode.BasicBlock bblock);
    void visit(LoopTreeNode.Loop loop);
    void visit(LoopTreeNode.IfStatement ifStatement);
}
