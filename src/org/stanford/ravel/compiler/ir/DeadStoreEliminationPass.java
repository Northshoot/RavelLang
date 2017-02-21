package org.stanford.ravel.compiler.ir;

import org.stanford.ravel.compiler.ir.typed.TBlock;
import org.stanford.ravel.compiler.ir.typed.TFieldStore;
import org.stanford.ravel.compiler.ir.typed.TInstruction;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.types.CompoundType;
import org.stanford.ravel.compiler.types.ModelType;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

/**
 * Like dead value elimination, but also eliminates field stores to
 * records that don't alias
 *
 * The goal is to eliminate dumb code that sets a field but does not
 * save the record, which would confuse the operation analysis
 *
 * Created by gcampagn on 2/15/17.
 */
public class DeadStoreEliminationPass {
    private final Set<Integer> liveVariables = new HashSet<>();
    private final TypedIR ir;
    private boolean progress;
    private boolean madeChanges;

    public DeadStoreEliminationPass(TypedIR ir) {
        this.ir = ir;
    }

    public boolean run() {
        madeChanges = false;
        liveVariables.clear();
        // the return value is always live
        liveVariables.add(Registers.RETURN_REG);

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
        if (instr instanceof TFieldStore) {
            CompoundType compoundType = ((TFieldStore) instr).compoundType;
            if (compoundType instanceof ModelType.RecordType) {
                int record = ((TFieldStore) instr).object;

                if (liveVariables.contains(record))
                    return true;
                for (int alias : ir.getAliases(record)) {
                    if (liveVariables.contains(alias))
                        return true;
                }
                return false;
            }

            return true;
        }

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
