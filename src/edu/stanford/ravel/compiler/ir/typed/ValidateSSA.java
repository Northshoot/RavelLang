package edu.stanford.ravel.compiler.ir.typed;

import edu.stanford.ravel.compiler.ir.Registers;

import java.util.HashMap;
import java.util.Map;

/**
 * Validate that the generated IR is in SSA form
 *
 * Created by gcampagn on 2/8/17.
 */
public class ValidateSSA {
    public static void validate(TypedIR ir) {
        Map<Integer, TInstruction> definition = new HashMap<>();

        ControlFlowGraph cfg = ir.getControlFlowGraph();
        for (TBlock block : cfg) {
            for (TInstruction instruction : block) {
                int sink = instruction.getSink();
                if (sink == Registers.VOID_REG)
                    continue;

                if (definition.containsKey(sink))
                    throw new AssertionError("Double definition of " + sink + ", first seen at " + definition.get(sink) + ", now at " + instruction);
                definition.put(sink, instruction);

                if (instruction instanceof TPhi) {
                    // verify that phi sources don't refer to other variables in the same block
                    // verify that phi sources are indeed defined

                    int[] sources = instruction.getSources();
                    TBlock[] blocks = ((TPhi) instruction).getSourceBlocks();
                    for (int i = 0; i < sources.length; i++) {
                        assert blocks[i] != block;

                        boolean found = false;
                        for (TInstruction inSourceBlock : blocks[i]) {
                            if (inSourceBlock.getSink() == sources[i]) {
                                found = true;
                                break;
                            }
                        }
                        if (ir.isParameter(sources[i])) {
                            found = true;
                        }
                        assert found;

                        // the source definition might be later in the code
                        // (eg in the body of a loop)
                        //TInstruction sourceDef = definition.get(sources[i]);
                        //assert sourceDef != null;
                    }
                }
            }
        }
    }
}
