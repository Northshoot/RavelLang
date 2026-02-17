package ai.harmony.ravel.analysis;

import ai.harmony.ravel.RavelApplication;
import ai.harmony.ravel.RavelCompiler;
import ai.harmony.ravel.compiler.ir.typed.TInstruction;
import ai.harmony.ravel.compiler.ir.typed.TMethodCall;
import ai.harmony.ravel.compiler.types.ModelType;
import ai.harmony.ravel.primitives.Controller;
import ai.harmony.ravel.primitives.EventHandler;
import ai.harmony.ravel.primitives.Model;

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
