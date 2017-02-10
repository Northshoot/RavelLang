package org.stanford.ravel.compiler.ir;

import org.stanford.ravel.compiler.ir.typed.TBlock;
import org.stanford.ravel.compiler.ir.typed.TInstruction;
import org.stanford.ravel.compiler.ir.typed.TypedIR;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

/**
 * Eliminates instructions with no side effects that
 * compute unused values.
 *
 * Created by gcampagn on 2/9/17.
 */
class DeadValueEliminationPass {
    private final Set<Integer> liveVariables = new HashSet<>();
    private final TypedIR ir;
    private boolean progress;
    private boolean madeChanges;

    public DeadValueEliminationPass(TypedIR ir) {
        this.ir = ir;
    }

    public boolean run() {
        madeChanges = false;

        // first compute the dead values
        do {
            progress = false;
            ir.getControlFlowGraph().visitBackward(this::visitAnalysis);
        } while(progress);

        // then eliminate the dead instructions
        ir.getControlFlowGraph().visitBackward(this::visitEliminate);
        return madeChanges;
    }

    private boolean isAlive(TInstruction instr) {
        if (instr.writesMemory() || instr.affectsControlFlow())
            return true;

        int sink = instr.getSink();
        if (sink != Registers.VOID_REG)
            return liveVariables.contains(sink);
        else
            return false;
    }

    private void visitAnalysis(TBlock block) {
        ListIterator<TInstruction> li = block.listIteratorAtLast();
        while (li.hasPrevious()) {
            TInstruction instr = li.previous();

            // If instr writes memory (which includes all function calls), then
            // all its sources are live
            // If the sink of the instruction is live, then all its sources are
            // live
            // If the instruction affects control flow (if statement, break, continue), then all its sources are live
            if (isAlive(instr)) {
                for (int src : instr.getSources()) {
                    progress = liveVariables.add(src) || progress;
                }
            }
        }
    }

    private void visitEliminate(TBlock block) {
        ListIterator<TInstruction> li = block.listIteratorAtLast();
        while (li.hasPrevious()) {
            TInstruction instr = li.previous();

            if (!isAlive(instr)) {
                li.remove();
                madeChanges = true;
            }
        }
    }
}
