package ai.harmony.ravel.compiler.ir.typed;

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

    public void buildForwardBackward() {
        blocks.clear();
        Set<TBlock> visited = new HashSet<>();

        topologicalSort(visited, entry);
        Collections.reverse(blocks);
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

        block.setSequenceId(blocks.size());
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
