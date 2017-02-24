package org.stanford.ravel.compiler.ir;

import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.error.FatalCompilerErrorException;

/**
 * Lower IR before it is consumed by code generation backends.
 *
 * Created by gcampagn on 2/9/17.
 */
public class LowerIRPass {
    private final boolean debug;
    private final RavelCompiler driver;
    private boolean hadError = false;

    public LowerIRPass(RavelCompiler driver, boolean debug) {
        this.driver = driver;
        this.debug = debug;
    }

    public void run(TypedIR ir) throws FatalCompilerErrorException {
        OutofSSAPass outofSSA = new OutofSSAPass(ir);
        outofSSA.run();

        if (debug) {
            System.out.println("Ouf of SSA CFG");
            ir.getControlFlowGraph().visitForward(System.out::println);
        }
    }
}
