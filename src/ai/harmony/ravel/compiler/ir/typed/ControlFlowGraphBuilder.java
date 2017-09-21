package ai.harmony.ravel.compiler.ir.typed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcampagn on 1/23/17.
 */
public class ControlFlowGraphBuilder {
    private ControlFlowGraph cfg;
    private List<TBlock> blockStack = new ArrayList<>();
    private int nextBlockId = 1;
    private TBlock currentBlock;

    public ControlFlowGraphBuilder() {
        cfg = new ControlFlowGraph();
        pushBlock(cfg.getEntry());
    }

    public ControlFlowGraph build() {
        if (currentBlock.getSuccessors().isEmpty()) {
            currentBlock.addSuccessor(getExit());
            getExit().addPredecessor(currentBlock);
        }
        cfg.buildForwardBackward();
        return cfg;
    }

    public TBlock getEntry() {
        return cfg.getEntry();
    }
    public TBlock getExit() {
        return cfg.getExit();
    }

    public TBlock newBlock() {
        return new TBlock(nextBlockId++);
    }

    public void pushBlock(TBlock block) {
        blockStack.add(block);
        currentBlock = block;
    }

    public void popBlock() {
        blockStack.remove(blockStack.size()-1);
        currentBlock = blockStack.get(blockStack.size()-1);
    }

    public void replaceBlock(TBlock block) {
        blockStack.set(blockStack.size()-1, block);
        currentBlock = block;
    }

    public void addInstruction(TInstruction instr) {
        currentBlock.add(instr);
    }

    public void addSuccessor(TBlock block) {
        currentBlock.addSuccessor(block);
        block.addPredecessor(currentBlock);
    }
}
