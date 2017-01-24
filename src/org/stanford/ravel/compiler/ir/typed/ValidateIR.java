package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.ir.typed.ControlFlowGraph;
import org.stanford.ravel.compiler.ir.typed.TBlock;
import org.stanford.ravel.compiler.ir.typed.TInstruction;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.Primitive;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/24/17.
 */
public class ValidateIR {
    public static void validate(TypedIR ir) {
        ControlFlowGraph cfg = ir.getControlFlowGraph();
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
                    assert ir.getRegisterType(sources[i]).equals(sourceTypes[i]);
                }
                if (sink != Registers.VOID_REG) {
                    assert Registers.isNormal(sink);
                    assert ir.getRegisterType(sink).equals(sinkType);
                } else {
                    assert sinkType == PrimitiveType.VOID;
                }
            }
        }

        for (TBlock block : cfg) {
            assert !block.getPredecessors().isEmpty() || block == cfg.getEntry();
            assert !block.getSuccessors().isEmpty() || block == cfg.getExit();
            for (TBlock successor : block.getSuccessors()) {
                assert successor.getPredecessors().contains(block);
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
    }
}
