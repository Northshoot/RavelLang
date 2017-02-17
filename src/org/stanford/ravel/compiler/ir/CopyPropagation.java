package org.stanford.ravel.compiler.ir;

import org.stanford.ravel.compiler.ir.typed.*;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by gcampagn on 2/16/17.
 */
public class CopyPropagation {
    private final Map<Integer, Integer> copies = new HashMap<>();
    private final Map<Integer, TBlock> copyBlocks = new HashMap<>();
    private final TypedIR ir;
    private boolean progress;
    private boolean madeChanges;

    public CopyPropagation(TypedIR ir) {
        this.ir = ir;
    }

    public boolean run() {
        madeChanges = false;
        copies.clear();
        copyBlocks.clear();

        do {
            progress = false;
            runOneStep();
        } while(progress);

        return madeChanges;
    }

    private void runOneStep() {
        ir.getControlFlowGraph().visitForward(this::visitBlock);
    }

    private int copyProp(int src) {
        Integer copy = copies.get(src);
        if (copy == null)
            return src;
        madeChanges = true;
        return copy;
    }

    private void addCopy(int tgt, int src, TBlock moveBlock) {
        Integer prev = copies.put(tgt, src);
        if (prev == null || prev != src)
            progress = true;
        copyBlocks.put(tgt, moveBlock);
    }

    private void visitBlock(TBlock block) {
        ListIterator<TInstruction> iter = block.listIterator();
        while (iter.hasNext()) {
            TInstruction instr = iter.next();
            instr.accept(new TInstructionVisitor() {
                @Override
                public void visit(TArrayLoad arrayLoad) {
                    arrayLoad.index = copyProp(arrayLoad.index);
                    arrayLoad.source = copyProp(arrayLoad.source);
                }

                @Override
                public void visit(TArrayStore arrayStore) {
                    arrayStore.object = copyProp(arrayStore.object);
                    arrayStore.value = copyProp(arrayStore.value);
                    arrayStore.index = copyProp(arrayStore.index);
                }

                @Override
                public void visit(TBinaryArithOp arithOp) {
                    arithOp.src1 = copyProp(arithOp.src1);
                    arithOp.src2 = copyProp(arithOp.src2);
                }

                @Override
                public void visit(TBreak breakInstr) {

                }

                @Override
                public void visit(TComparisonOp compOp) {
                    compOp.src1 = copyProp(compOp.src1);
                    compOp.src2 = copyProp(compOp.src2);
                }

                @Override
                public void visit(TContinue continueInstr) {

                }

                @Override
                public void visit(TConvert convert) {
                    convert.source = copyProp(convert.source);
                }

                @Override
                public void visit(TFieldLoad fieldLoad) {
                    fieldLoad.source = copyProp(fieldLoad.source);
                }

                @Override
                public void visit(TFieldStore fieldStore) {
                    fieldStore.object = copyProp(fieldStore.object);
                    fieldStore.value = copyProp(fieldStore.value);
                }

                @Override
                public void visit(TIfStatement ifStatement) {
                    ifStatement.cond = copyProp(ifStatement.cond);
                }

                @Override
                public void visit(TImmediateLoad immediateLoad) {
                }

                @Override
                public void visit(TMethodCall methodCall) {
                    for (int i = 0; i < methodCall.arguments.length; i++)
                        methodCall.arguments[i] = copyProp(methodCall.arguments[i]);
                }

                @Override
                public void visit(TMove move) {
                    TBlock copyBlock;

                    Integer copy = copies.get(move.source);
                    if (copy == null) {
                        copyBlock = block;
                    } else {
                        copyBlock = copyBlocks.get(move.source);
                        move.source = copy;
                        madeChanges = true;
                    }

                    addCopy(move.target, move.source, copyBlock);
                }

                @Override
                public void visit(TUnaryArithOp arithOp) {
                    arithOp.source = copyProp(arithOp.source);
                }

                @Override
                public void visit(TPhi phi) {
                    int src0 = Registers.UNSET_REG;
                    boolean allEqual = true;
                    assert phi.sources.length > 0;

                    for (int i = 0; i < phi.sources.length; i++) {
                        Integer copy = copies.get(phi.sources[i]);
                        if (copy != null) {
                            TBlock copyBlock = copyBlocks.get(phi.sources[i]);
                            phi.sources[i] = copy;
                            phi.blocks[i] = copyBlock;
                            madeChanges = true;
                        }

                        if (i == 0)
                            src0 = phi.sources[i];
                        else
                            allEqual = allEqual && phi.sources[i] == src0;
                    }

                    if (allEqual) {
                        iter.set(new TMove(phi.type, phi.target, src0));
                        addCopy(phi.target, src0, phi.blocks[0]);
                    }
                }
            });
        }
    }
}
