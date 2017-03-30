package edu.stanford.ravel.compiler.ir;

import edu.stanford.ravel.compiler.ir.typed.ControlFlowGraph;
import edu.stanford.ravel.compiler.ir.typed.TypedIR;
import edu.stanford.ravel.compiler.ir.typed.ValidateIR;
import edu.stanford.ravel.compiler.ir.typed.ValidateSSA;

import java.util.Map;
import java.util.Set;

/**
 * Wrap all IR optimizations in a single pass
 *
 * Created by gcampagn on 2/21/17.
 */
public class OptimizePass {
    private final boolean debug;
    private final TypedIR ir;

    public OptimizePass(TypedIR ir, boolean debug) {
        this.ir = ir;
        this.debug = debug;
    }

    public void run() {
        ControlFlowGraph cfg = ir.getControlFlowGraph();

        IntoSSAPass intoSSA = new IntoSSAPass(ir);
        intoSSA.run();

        ValidateIR.validate(ir);
        ValidateSSA.validate(ir);
        if (debug) {
            System.out.println("SSA CFG");
            cfg.visitForward(System.out::println);
        }

        // run local (non interprocedural) analysis and optimization passes

        DeadValueEliminationPass deadValueEliminationPass = new DeadValueEliminationPass(ir);
        DeadStoreEliminationPass deadStoreEliminationPass = new DeadStoreEliminationPass(ir);
        DeadControlFlowElimination deadControlFlowElimination = new DeadControlFlowElimination(ir);
        ConstantFolding constantFolding = new ConstantFolding(ir);
        CopyPropagation copyPropagation = new CopyPropagation(ir);
        AliasAnalysis aliasAnalysis = new AliasAnalysis(ir);

        boolean progress;
        int pass = 0;
        do {
            progress = false;

            // run constant folding first (which helps dead value elimination)
            progress = constantFolding.run() || progress;
            ValidateIR.validate(ir);
            // run dead control flow second (which helps dead value elimination)
            progress = deadControlFlowElimination.run() || progress;
            ValidateIR.validate(ir);
            // run copy propagation (which helps the alias analysis and the dead value elimination)
            //progress = copyPropagation.run() || progress;
            //ValidateIR.validate(ir);
            //ValidateSSA.validate(ir);
            // run dead value elimination third (which also helps the alias analysis)
            progress = deadValueEliminationPass.run() || progress;
            ValidateIR.validate(ir);

            // run alias analysis for record variables, which will be used by the security analysis
            Map<Integer, Set<Integer>> aliasResult = aliasAnalysis.run();
            ir.setAliases(aliasResult);
            if (debug) {
                System.out.println("Alias analysis");
                aliasResult.forEach((var, alias) -> {
                    System.out.println(var + ": " + alias);
                });
                System.out.println();
            }

            // run dead store elimination with the alias analysis
            progress = deadStoreEliminationPass.run() || progress;
            ValidateIR.validate(ir);

            if (debug && progress) {
                System.out.println("Opt pass #" + (pass+1));
                cfg.visitForward(System.out::println);
            }
            pass++;
        } while(progress);

        if (debug) {
            System.out.println("Final Loop Tree");
            System.out.println(ir.getLoopTree());
        }

        // remove unused variables
        UnusedVariableRemovalPass variableRemovalPass = new UnusedVariableRemovalPass(ir);
        variableRemovalPass.run();
    }
}
