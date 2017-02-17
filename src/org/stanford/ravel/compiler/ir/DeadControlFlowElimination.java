package org.stanford.ravel.compiler.ir;

import org.stanford.ravel.compiler.ir.typed.*;

import java.util.*;

/**
 * Eliminate if statements with constant branches
 *
 * Created by gcampagn on 2/16/17.
 */
public class DeadControlFlowElimination {
    private final TypedIR ir;
    private final Map<Integer, Object> constants = new HashMap<>();
    private final Set<TBlock> destroyedBlocks = new HashSet<>();
    private boolean madeChanges;

    public DeadControlFlowElimination(TypedIR ir) {
        this.ir = ir;
    }

    public boolean run() {
        madeChanges = false;
        constants.clear();

        runRecursive((LoopTreeNode.Block) ir.getLoopTree());
        if (madeChanges) {
            ir.getControlFlowGraph().buildForwardBackward();
            ir.getControlFlowGraph().visitForward(this::adjustPhis);
        }
        collapseBlocks((LoopTreeNode.Block) ir.getLoopTree());

        return madeChanges;
    }

    private void collapseBlocks(LoopTreeNode.Block block) {
        ListIterator<LoopTreeNode> children = block.listIterator();
        while (children.hasNext()) {
            LoopTreeNode child = children.next();

            if (child instanceof LoopTreeNode.Block) {
                if (((LoopTreeNode.Block) child).size() == 1) {
                    children.set(((LoopTreeNode.Block) child).get(0));
                    madeChanges = true;
                }
            }
        }
    }

    private void collectConstants(TBlock block) {
        for (TInstruction instr : block) {
            if (instr instanceof TImmediateLoad)
                constants.put(instr.getSink(), ((TImmediateLoad) instr).value);
        }
    }

    private void adjustPhis(TBlock bblock) {
        ListIterator<TInstruction> iter = bblock.listIterator();
        while (iter.hasNext()) {
            TInstruction instr = iter.next();
            if (instr instanceof TPhi) {
                TPhi phi = (TPhi)instr;
                int sources[] = phi.sources;
                TBlock blocks[] = phi.blocks;

                int toRemove = 0;
                for (TBlock block : blocks) {
                    if (destroyedBlocks.contains(block))
                        toRemove++;
                }
                if (toRemove == 0)
                    continue;
                if (toRemove == sources.length) {
                    iter.remove();
                    continue;
                }

                int[] newSources = new int[sources.length-toRemove];
                TBlock[] newBlocks = new TBlock[sources.length-toRemove];
                int j = 0;
                for (int i = 0; i < sources.length; i++) {
                    if (destroyedBlocks.contains(blocks[i]))
                        continue;
                    newSources[j] = sources[i];
                    newBlocks[j] = blocks[i];
                    j++;
                }
                phi.sources = newSources;
                phi.blocks = newBlocks;
                if (phi.sources.length == 1) {
                    iter.set(new TMove(phi.type, phi.target, phi.sources[0]));
                }
            }
        }
    }

    private void destroyRecursive(LoopTreeNode node) {
        if (node instanceof LoopTreeNode.BasicBlock) {
            ((LoopTreeNode.BasicBlock) node).getBlock().destroy();
            destroyedBlocks.add(((LoopTreeNode.BasicBlock) node).getBlock());
        }

        ListIterator<LoopTreeNode> children = node.listIterator();
        while (children.hasNext()) {
            LoopTreeNode child = children.next();

            destroyRecursive(child);
        }
    }

    private TBlock findFirstBlock(LoopTreeNode node) {
        if (node instanceof LoopTreeNode.BasicBlock)
            return ((LoopTreeNode.BasicBlock) node).getBlock();

        ListIterator<LoopTreeNode> children = node.listIterator();
        while (children.hasNext()) {
            LoopTreeNode child = children.next();
            return findFirstBlock(child);
        }

        // should never happen
        return null;
    }

    private void removeIfInstruction(LoopTreeNode previous, LoopTreeNode.IfStatement ifStatement) {
        TBlock block = findFirstBlock(ifStatement);
        assert block != null;

        Set<TBlock> predecessors = block.getPredecessors();
        assert predecessors.size() == 1;

        for (TBlock pred : predecessors) {
            ListIterator<TInstruction> iter = pred.listIterator();
            while (iter.hasNext()) {
                TInstruction instr = iter.next();
                if (instr == ifStatement.getIfInstruction())
                    iter.remove();
            }
        }
    }

    private void runRecursive(LoopTreeNode.Block block) {
        ListIterator<LoopTreeNode> children = block.listIterator();
        while (children.hasNext()) {
            LoopTreeNode child = children.next();

            if (child instanceof LoopTreeNode.BasicBlock) {
                TBlock bblock = ((LoopTreeNode.BasicBlock) child).getBlock();
                collectConstants(bblock);

                if (bblock.isEmpty() && bblock != ir.getControlFlowGraph().getExit()
                        && bblock != ir.getControlFlowGraph().getEntry()) {
                    destroyedBlocks.add(bblock);
                    // the block is empty so it cannot branch
                    assert bblock.getSuccessors().size() == 1;
                    bblock.destroy();
                    for (TBlock pred : bblock.getPredecessors()) {
                        for (TBlock succ : bblock.getSuccessors()) {
                            succ.addPredecessor(pred);
                            pred.addSuccessor(succ);
                        }
                    }

                    children.remove();
                    madeChanges = true;
                }
            } else if (child instanceof LoopTreeNode.Loop) {
                runRecursiveSkip(child);
            } else if (child instanceof LoopTreeNode.IfStatement) {
                int cond = ((LoopTreeNode.IfStatement) child).getCondition();
                Object bool = constants.get(cond);
                if (bool != null) {
                    LoopTreeNode previous = children.previous();
                    child = children.next();
                    removeIfInstruction(previous, (LoopTreeNode.IfStatement)child);
                    if ((boolean)bool) {
                        children.set(((LoopTreeNode.IfStatement) child).getIftrue());
                        destroyRecursive(((LoopTreeNode.IfStatement) child).getIffalse());
                    } else {
                        children.set(((LoopTreeNode.IfStatement) child).getIffalse());
                        destroyRecursive(((LoopTreeNode.IfStatement) child).getIftrue());
                    }
                    madeChanges = true;
                } else {
                    LoopTreeNode ifTrue = ((LoopTreeNode.IfStatement) child).getIftrue();
                    LoopTreeNode ifFalse = ((LoopTreeNode.IfStatement) child).getIffalse();

                    // if both branches are empty, then delete the if statement altoghether
                    if (ifTrue instanceof LoopTreeNode.BasicBlock &&
                            ((LoopTreeNode.BasicBlock) ifTrue).getBlock().isEmpty() &&
                        ifFalse instanceof LoopTreeNode.BasicBlock &&
                            ((LoopTreeNode.BasicBlock) ifFalse).getBlock().isEmpty()) {
                        LoopTreeNode previous = children.previous();
                        child = children.next();
                        removeIfInstruction(previous, (LoopTreeNode.IfStatement)child);

                        TBlock ifTrueBlock = ((LoopTreeNode.BasicBlock) ifTrue).getBlock();
                        TBlock ifFalseBlock = ((LoopTreeNode.BasicBlock) ifFalse).getBlock();
                        destroyedBlocks.add(ifTrueBlock);
                        destroyedBlocks.add(ifFalseBlock);
                        assert ifTrueBlock.getPredecessors().equals(ifFalseBlock.getPredecessors());
                        // the blocks are empty so they cannot branch
                        assert ifTrueBlock.getSuccessors().size() == 1;
                        assert ifFalseBlock.getSuccessors().size() == 1;
                        assert ifTrueBlock.getSuccessors().equals(ifFalseBlock.getSuccessors());
                        ifTrueBlock.destroy();
                        ifFalseBlock.destroy();
                        for (TBlock pred : ifTrueBlock.getPredecessors()) {
                            for (TBlock succ : ifTrueBlock.getSuccessors()) {
                                succ.addPredecessor(pred);
                                pred.addSuccessor(succ);
                            }
                        }

                        children.remove();
                        madeChanges = true;
                    }
                }
            } else if (child instanceof LoopTreeNode.Block) {
                runRecursive((LoopTreeNode.Block)child);
                if (((LoopTreeNode.Block) child).isEmpty())
                    children.remove();
            }
        }
    }

    private void runRecursiveSkip(LoopTreeNode node) {
        ListIterator<LoopTreeNode> children = node.listIterator();
        while (children.hasNext()) {
            LoopTreeNode child = children.next();

            if (child instanceof LoopTreeNode.BasicBlock) {
                collectConstants(((LoopTreeNode.BasicBlock) child).getBlock());
            } else if (child instanceof LoopTreeNode.Block) {
                runRecursive((LoopTreeNode.Block)child);
            } else {
                runRecursiveSkip(child);
            }
        }
    }
}
