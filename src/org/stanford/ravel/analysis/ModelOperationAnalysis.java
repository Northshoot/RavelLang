package org.stanford.ravel.analysis;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.primitives.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Compute which instantiated controller does what on each field of each model,
 * based on the instructions on the event handlers
 *
 * Created by gcampagn on 2/9/17.
 */
public class ModelOperationAnalysis {
    private final RavelCompiler driver;
    private final RavelApplication app;
    private final boolean debug;
    private boolean progress;

    private final Map<ConcreteController, Map<FieldTag, Operation>> operations = new HashMap<>();

    public ModelOperationAnalysis(RavelCompiler driver, RavelApplication app, boolean debug) {
        this.driver = driver;
        this.app = app;
        this.debug = debug;
    }

    public void run() {
        // now we know for each variable what model it comes from, so we can go through
        // the list of instructions in the code and find out, for each field, what operations
        // each controller does
        do {
            progress = false;
            runModelOperations();
        } while (progress);

        if (debug) {
            dumpAllOperations();
        }

        saveOperations();
    }

    private void saveOperations() {
        operations.forEach((ic, icOperations) -> {
            icOperations.forEach((fieldTag, op) -> {
                if (fieldTag.isLocal())
                    return;
                assert fieldTag.model != null;
                ModelField field = fieldTag.model.getField(fieldTag.field);
                assert field != null;

                field.addOperation(ic.getSpace(), fieldTag.creator, op);
            });
        });
    }

    private void dumpAllOperations() {
        System.out.println("Operations on each model field:");
        operations.forEach((ic, icOperations) -> {
            System.out.println("By " + ic);
            icOperations.forEach((field, op) -> {
                if (field.isLocal())
                    return;
                System.out.println(field + ": " + op);
            });
            System.out.println();
        });
    }

    private void runModelOperations() {
        for (Space s : app.getSpaces()) {
            for (ConcreteController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    LocalModelOperationAnalysis localAnalysis = new LocalModelOperationAnalysis(event, event.getHandler().getBody());
                    localAnalysis.run();
                    operations.computeIfAbsent(ic, (key) -> new HashMap<>()).putAll(localAnalysis.getOperations());
                }
            }
        }
    }
}
