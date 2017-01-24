package org.stanford.ravel.compiler.ir.typed;

import java.util.*;

/**
 * Created by gcampagn on 1/23/17.
 */
public class ControlFlowGraph implements Iterable<TBlock> {
    public static final int BLOCK_ENTRY = 0;
    public static final int BLOCK_EXIT = -1;

    private TBlock entry = new TBlock(BLOCK_ENTRY);
    private TBlock exit = new TBlock(BLOCK_EXIT);

    // in topological sort order from entry to exit
    private final List<TBlock> blocks = new ArrayList<>();

    void freeze() {
        Set<TBlock> visited = new HashSet<>();

        topologicalSort(visited, entry);
        for (int i = 0; i < blocks.size()/2; i++) {
             TBlock tmp = blocks.get(i);
             blocks.set(i, blocks.get(blocks.size()-1-i));
             blocks.set(blocks.size()-1-i, tmp);
        }
    }

    public TBlock getEntry() {
        return entry;
    }
    public TBlock getExit() {
        return exit;
    }

    private void topologicalSort(Set<TBlock> visited, TBlock block) {
        visited.add(block);

        for (TBlock next : block.getSuccessors()) {
            if (visited.contains(next))
                continue;
            topologicalSort(visited, next);
        }

        blocks.add(block);
    }

    public void visitForward(ControlFlowGraphVisitor visitor) {
        for (TBlock b : blocks)
            visitor.visitBlock(b);
    }
    public void visitBackward(ControlFlowGraphVisitor visitor) {
        for (int i = blocks.size()-1; i >= 0; i--)
            visitor.visitBlock(blocks.get(i));
    }

    @Override
    public Iterator<TBlock> iterator() {
        return blocks.iterator();
    }
}
