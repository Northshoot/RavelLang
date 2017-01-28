package org.stanford.ravel.compiler.ir.typed;

import org.antlr.v4.codegen.model.Loop;

/**
 * Created by gcampagn on 1/25/17.
 */
public class LoopTreeBuilder {
    private LoopTreeNode.Block root;

    private LoopTreeNode current;

    public LoopTreeBuilder() {
        root = new LoopTreeNode.Block();
        current = root;
    }

    public LoopTreeNode getRoot() {
        return root;
    }

    private LoopTreeNode.Block ensureCurrentBlock() {
        if (!(current instanceof LoopTreeNode.Block)) {
            LoopTreeNode.Block blockChild = new LoopTreeNode.Block();
            current.getParent().replaceChild(current, blockChild);
            blockChild.addChild(current);
            assert current.getParent() == blockChild;
            current = blockChild;
            assert blockChild.getParent() != null;
        }

        return ((LoopTreeNode.Block)current);
    }

    public void addBasicBlock(TBlock block) {
        ensureCurrentBlock().addChild(new LoopTreeNode.BasicBlock(block));
    }

    private void pop() {
        current = current.getParent();
        if (current == null)
            throw new IllegalStateException("invalid pop");
    }

    public void ifStatement(int cond, TBlock iftrueBlock, TBlock iffalseBlock) {
        LoopTreeNode iftrue = new LoopTreeNode.BasicBlock(iftrueBlock);
        LoopTreeNode iffalse = new LoopTreeNode.BasicBlock(iffalseBlock);

        ensureCurrentBlock().addChild(new LoopTreeNode.IfStatement(cond, iftrue, iffalse));
        current = iftrue;
    }

    public void elseStatement(int cond) {
        pop();
        if (!(current instanceof LoopTreeNode.IfStatement) || cond != ((LoopTreeNode.IfStatement) current).getCondition())
            throw new IllegalStateException("Not currently processing an if statement");
        current = ((LoopTreeNode.IfStatement)current).getIffalse();
    }

    public void endIfStatement(int cond) {
        pop();
        if (!(current instanceof LoopTreeNode.IfStatement) || cond != ((LoopTreeNode.IfStatement) current).getCondition())
            throw new IllegalStateException("Not currently processing an if statement");
        pop();
    }

    public void beginLoop(TBlock loopBody) {
        LoopTreeNode body = new LoopTreeNode.BasicBlock(loopBody);

        ensureCurrentBlock().addChild(new LoopTreeNode.Loop(body));
        current = body;
    }

    public void endLoop() {
        pop();
        if (!(current instanceof LoopTreeNode.Loop))
            throw new IllegalStateException("Not currently in a loop");
        pop();
    }
}
