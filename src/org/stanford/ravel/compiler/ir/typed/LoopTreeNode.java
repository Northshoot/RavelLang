package org.stanford.ravel.compiler.ir.typed;

import java.util.*;

/**
 * A version of the ControlFlowGraph that preserves the structure of the code
 * and makes loops evident to the translator.
 *
 * Created by gcampagn on 1/24/17.
 */
public abstract class LoopTreeNode {
    private LoopTreeNode parent = null;

    public static class BasicBlock extends LoopTreeNode {
        private final TBlock block;

        BasicBlock(TBlock block) {
            this.block = block;
        }

        public TBlock getBlock() {
            return block;
        }

        @Override
        public void accept(LoopTreeVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public void replaceChild(LoopTreeNode oldChild, LoopTreeNode newChild) {
            throw new IllegalArgumentException("Basic block loop tree nodes don't have children");
        }

        @Override
        public String toString() {
            return block.toString();
        }

        @Override
        public ListIterator<LoopTreeNode> listIterator() {
            return Collections.emptyListIterator();
        }
    }

    public static class Block extends LoopTreeNode {
        private final List<LoopTreeNode> children = new ArrayList<>();

        void addChild(LoopTreeNode node) {
            children.add(node);
            node.setParent(this);
        }

        @Override
        public void accept(LoopTreeVisitor visitor) {
            for (LoopTreeNode node : children)
                node.accept(visitor);
        }

        @Override
        public void replaceChild(LoopTreeNode oldChild, LoopTreeNode newChild) {
            ListIterator<LoopTreeNode> li = children.listIterator();
            boolean found = false;
            while (li.hasNext()) {
                LoopTreeNode elem = li.next();
                if (elem == oldChild) {
                    li.set(newChild);
                    newChild.setParent(this);
                    found = true;
                    break;
                }
            }
            if (!found)
                throw new IllegalArgumentException("Invalid child passed to replaceChild");
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (LoopTreeNode node : children)
                builder.append(node.toString());
            return builder.toString();
        }

        @Override
        public ListIterator<LoopTreeNode> listIterator() {
            return children.listIterator();
        }

        public boolean isEmpty() {
            return children.isEmpty();
        }
    }

    public static class IfStatement extends LoopTreeNode {
        private final TIfStatement cond;
        private LoopTreeNode iftrue;
        private LoopTreeNode iffalse;

        IfStatement(TIfStatement cond, LoopTreeNode iftrue, LoopTreeNode iffalse) {
            this.cond = cond;
            this.iftrue = iftrue;
            this.iffalse = iffalse;
            iftrue.setParent(this);
            iffalse.setParent(this);
        }

        public int getCondition() {
            return cond.cond;
        }

        public TIfStatement getIfInstruction() {
            return cond;
        }

        public LoopTreeNode getIftrue() {
            return iftrue;
        }

        public LoopTreeNode getIffalse() {
            return iffalse;
        }

        @Override
        public void accept(LoopTreeVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public void replaceChild(LoopTreeNode oldChild, LoopTreeNode newChild) {
            if (oldChild == iftrue) {
                iftrue = newChild;
                newChild.setParent(this);
                return;
            }
            if (oldChild == iffalse) {
                iffalse = newChild;
                newChild.setParent(this);
                return;
            }
            throw new IllegalArgumentException("Invalid child passed to replaceChild");
        }

        @Override
        public String toString() {
            return "if " + cond.cond + " {\n" + iftrue + "} else {" + iffalse + "}\n";
        }

        @Override
        public ListIterator<LoopTreeNode> listIterator() {
            return Arrays.asList(iftrue, iffalse).listIterator();
        }
    }

    public static class Loop extends LoopTreeNode {
        private LoopTreeNode body;

        Loop(LoopTreeNode body) {
            this.body = body;
            body.setParent(this);
        }

        public LoopTreeNode getBody() {
            return body;
        }

        @Override
        public void accept(LoopTreeVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public void replaceChild(LoopTreeNode oldChild, LoopTreeNode newChild) {
            if (oldChild == body) {
                body = newChild;
                newChild.setParent(this);
                return;
            }
            throw new IllegalArgumentException("Invalid child passed to replaceChild");
        }

        @Override
        public String toString() {
            return "loop {\n" + body + "}\n";
        }

        @Override
        public ListIterator<LoopTreeNode> listIterator() {
            return Collections.singletonList(body).listIterator();
        }
    }

    public abstract void accept(LoopTreeVisitor visitor);

    private void setParent(LoopTreeNode parent) {
        this.parent = parent;
    }

    LoopTreeNode getParent() {
        return parent;
    }

    public abstract void replaceChild(LoopTreeNode oldChild, LoopTreeNode newChild);

    public abstract ListIterator<LoopTreeNode> listIterator();
}
