package edu.stanford.ravel.compiler.ir;

import edu.stanford.ravel.compiler.ir.typed.TInstruction;
import edu.stanford.ravel.compiler.ir.typed.TypedIR;

import java.util.HashSet;
import java.util.Set;

/**
 * Removes (undeclares) variables that are completely unused.
 *
 * This has no effect on generated code (after backend compilation),
 * but it makes the generated code more readable.
 *
 * Created by gcampagn on 2/27/17.
 */
public class UnusedVariableRemovalPass {
    private final TypedIR ir;
    private final Set<Integer> used = new HashSet<>();

    public UnusedVariableRemovalPass(TypedIR ir) {
        this.ir = ir;
    }

    public void run() {
        // parameters are always used
        for (int param : ir.getParameters())
            used.add(param);
        // the return value is always used
        used.add(Registers.RETURN_REG);
        // self is always used
        used.add(Registers.SELF_REG);

        ir.getControlFlowGraph().visitForward((block) -> {
            for (TInstruction instr : block) {
                used.add(instr.getSink());
                for (int source : instr.getSources())
                    used.add(source);
            }
        });

        ir.retainVariables(used);
    }
}
