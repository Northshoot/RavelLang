package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

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
                    // FIXME change this assert when you change PrimitiveType.ANY.isAssignable back to strict equality
                    //assert sourceTypes[i] != PrimitiveType.ERROR && sourceTypes[i] != PrimitiveType.VOID
                    //        && sourceTypes[i] != PrimitiveType.ANY;
                    assert sourceTypes[i] != PrimitiveType.ERROR && sourceTypes[i] != PrimitiveType.VOID;
                    assert ir.getRegisterType(sources[i]).equals(sourceTypes[i]);
                }
                if (sink != Registers.VOID_REG) {
                    assert Registers.isNormal(sink);
                    assert ir.getRegisterType(sink).equals(sinkType);

                    // this is not true always:
                    // AstToUntypedIRVisitor will assign registers to the result of function calls,
                    // even if those calls return void, in which case TypeResolvePass will set those
                    // registers to VOID
                    // if the user attempts to do anything with the result it will result in a type error
                    // though
                    assert sinkType != PrimitiveType.VOID;
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
    }
}
