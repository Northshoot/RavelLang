package org.stanford.ravel.analysis;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.ir.typed.TInstruction;
import org.stanford.ravel.compiler.ir.typed.TMethodCall;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.primitives.Controller;
import org.stanford.ravel.primitives.EventHandler;
import org.stanford.ravel.primitives.Model;

/**
 * Find all the controllers that write to a model
 * (in the sense that they call .save() on that model)
 *
 * Created by gcampagn on 2/10/17.
 */
public class ModelWritingAnalysis {
    private final RavelCompiler driver;
    private final RavelApplication app;

    public ModelWritingAnalysis(RavelCompiler driver, RavelApplication app) {
        this.driver = driver;
        this.app = app;
    }

    private void runEventHandler(Controller c, EventHandler handler) {
        handler.getBody().getControlFlowGraph().visitForward((block) -> {
            for (TInstruction instr : block) {
                if (instr instanceof TMethodCall) {
                    TMethodCall methodCall = (TMethodCall)instr;

                    if (methodCall.type.getOwner() instanceof ModelType &&
                            methodCall.type.getFunctionName().equals("save")) {
                        Model m = app.getModel(methodCall.type.getOwner().getName());
                        assert m != null;

                        c.addWrittenToModel(m);
                    }
                }
            }
        });
    }

    public void run() {
        for (Controller c : app.getControllers()) {
            for (EventHandler handler : c) {
                runEventHandler(c, handler);
            }
        }
    }
}