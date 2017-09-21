package ai.harmony.ravel.compiler.ir.typed;

import ai.harmony.ravel.compiler.ir.Registers;
import ai.harmony.ravel.compiler.types.PrimitiveType;
import ai.harmony.ravel.compiler.types.Type;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gcampagn on 1/24/17.
 */
public class ValidateIR {
    public static void validate(TypedIR ir) {
        ControlFlowGraph cfg = ir.getControlFlowGraph();
        Set<TBlock> fromCfg = new HashSet<>();
        Set<TBlock> fromBackwardCfg = new HashSet<>();

        for (TBlock block : cfg)
            fromCfg.add(block);
        cfg.visitBackward(fromBackwardCfg::add);
        assert fromBackwardCfg.equals(fromCfg);

        for (TBlock block : cfg) {
            for (TInstruction instruction : block) {
                int[] sources = instruction.getSources();
                Type[] sourceTypes = instruction.getSourceTypes();
                assert sources.length == sourceTypes.length;

                int sink = instruction.getSink();
                Type sinkType = instruction.getSinkType();

                for (int i = 0; i < sources.length; i++) {
                    assert Registers.isNormal(sources[i]);
                    assert sourceTypes[i] != PrimitiveType.ERROR && sourceTypes[i] != PrimitiveType.VOID
                            && sourceTypes[i] != PrimitiveType.ANY;
                    assert sourceTypes[i] != PrimitiveType.ERROR && sourceTypes[i] != PrimitiveType.VOID;
                    assert ir.getRegisterType(sources[i]).equals(sourceTypes[i]);
                }
                if (sink != Registers.VOID_REG) {
                    assert Registers.isNormal(sink);
                    assert ir.getRegisterType(sink).equals(sinkType);

                    assert sinkType != PrimitiveType.VOID;
                } else {
                    assert sinkType == PrimitiveType.VOID;
                }

                if (instruction instanceof TPhi) {
                    for (TBlock definer : ((TPhi) instruction).blocks) {
                        assert fromCfg.contains(definer);
                    }
                }
            }
        }

        for (TBlock block : cfg) {
            assert !block.getPredecessors().isEmpty() || block == cfg.getEntry();
            assert !block.getSuccessors().isEmpty() || block == cfg.getExit();
            for (TBlock successor : block.getSuccessors()) {
                assert successor.getPredecessors().contains(block);
            }
            for (TBlock successor : block.getPredecessors()) {
                assert successor.getSuccessors().contains(block);
            }

            // enforce structured control flow
            TInstruction instr = block.getLastInstruction();
            if (instr instanceof TIfStatement)
                assert block.getSuccessors().size() == 2;
            else
                assert block.getSuccessors().size() == 1 || block == cfg.getExit();
        }
        assert cfg.getEntry().getPredecessors().isEmpty();
        assert cfg.getExit().getSuccessors().isEmpty();
        assert cfg.getExit().isEmpty();

        Set<TBlock> fromLoopTree = new HashSet<>();
        ir.getLoopTree().accept(new LoopTreeVisitor() {
            @Override
            public void visit(LoopTreeNode.BasicBlock bblock) {
                fromLoopTree.add(bblock.getBlock());
            }

            @Override
            public void visit(LoopTreeNode.Loop loop) {
                loop.getBody().accept(this);
            }

            @Override
            public void visit(LoopTreeNode.IfStatement ifStatement) {
                ifStatement.getIftrue().accept(this);
                ifStatement.getIffalse().accept(this);
            }
        });
        assert fromCfg.equals(fromLoopTree);
    }
}
