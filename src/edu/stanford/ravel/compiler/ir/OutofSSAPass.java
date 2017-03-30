package edu.stanford.ravel.compiler.ir;

import edu.stanford.ravel.compiler.ir.typed.*;

import java.util.Iterator;

/**
 * Converts an IR in SSA form into an IR with no phi nodes
 *
 * The algorithm is pretty simple: for every phi node, we know
 * which basic block the value comes from, so we add a move from
 * phi source to the phi dest at the end of that block
 *
 * Created by gcampagn on 2/8/17.
 */
class OutofSSAPass {
    private final TypedIR ir;

    OutofSSAPass(TypedIR ir) {
        this.ir = ir;
    }

    public void run() {
        ir.getControlFlowGraph().visitForward(this::visitBlock);
    }

    private void visitBlock(TBlock block) {
        Iterator<TInstruction> iter = block.iterator();
        while (iter.hasNext()) {
            TInstruction instr = iter.next();
            if (instr instanceof TPhi) {
                int[] sources = ((TPhi) instr).getSources();
                TBlock[] blocks = ((TPhi) instr).getSourceBlocks();
                for (int i = 0; i < sources.length; i++) {
                    assert blocks[i] != block;

                    blocks[i].add(new TMove(((TPhi) instr).getPhiType(), ((TPhi) instr).target, sources[i]));
                }
                iter.remove();
            }
        }
    }
}
