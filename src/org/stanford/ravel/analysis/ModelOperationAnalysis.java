package org.stanford.ravel.analysis;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.ir.typed.TInstruction;
import org.stanford.ravel.compiler.ir.typed.TMethodCall;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.types.FunctionType;
import org.stanford.ravel.compiler.types.ModelType;
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

    private final Map<InstantiatedController, Map<FieldTag, Operation>> operations = new HashMap<>();

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

        runWriteAnalysis();

        if (debug) {
            dumpAllWrites();
        }

        saveOperations();
    }

    private void runWriteAnalysis() {
        for (Space s : app.getSpaces()) {
            for (InstantiatedController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    runLocalWriteAnalysis(ic, event, event.getHandler().getBody());
                }
            }
        }
    }

    private void addWrite(InstantiatedController ic, ModelType model, String fieldName) {
        ModelField field = app.getModel(model.getName()).getField(fieldName);
        assert field != null;
        field.addWriter(ic.getSpace());
    }

    private void runLocalWriteAnalysis(InstantiatedController ic, LinkedEvent event, TypedIR body) {
        body.getControlFlowGraph().visitForward((block) -> {
            for (TInstruction instr : block) {
                if (instr instanceof TMethodCall) {
                    FunctionType type = ((TMethodCall) instr).type;
                    if (type.getOwner() instanceof ModelType && type.getFunctionName().equals("save")) {
                        int record = instr.getSources()[0];

                        // for each field in record, there will be zero, 1, or multiple field tags
                        // zero field tags means that we've never seen a read or a write of this field
                        // anywhere in the code (which means we're free to do whatever we like)
                        // multiple field tags means that multiple spaces write to this field, or they
                        // write values coming from multiple places
                        //
                        // we mark this space as a writer for this field if we see that it writes a
                        // value coming from this space (based on the creator part of the field tag),
                        // or if we see that one of the fields requires Operation.ANY, which
                        // means it will be in plain text anyway
                        //
                        // (being a writer in this context means that we have to produce brand new
                        // cyphertext for a field, as opposed to computing on cyphertexts)
                        ModelType.RecordType recordType = ((ModelType) type.getOwner()).getRecordType();

                        int recordVar = instr.getSources()[0];
                        for (FieldTag tag : event.getVariableFieldTags(recordVar)) {
                            if (tag.creator == ic.getSpace()) {
                                // NOTE: do not use tag.model here, we don't care what model the value
                                // of the field comes from, we care about what model we're writing right
                                // now
                                addWrite(ic, (ModelType) type.getOwner(), tag.field);
                            }
                        }

                        for (String fieldName : recordType.getMemberList()) {
                            ModelField field = app.getModel(recordType.getModel().getName()).getField(fieldName);
                            field.getOperations(ic.getSpace()).forEach((creator, op) -> {
                                if (creator == ic.getSpace())
                                    return;
                                if (op == Operation.ANY)
                                    addWrite(ic, (ModelType) type.getOwner(), field.getName());
                            });
                        }
                    }
                }
            }
        });
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

    private void dumpAllWrites() {
        System.out.println("Writes on all model fields:");
        for (Model m : app.getModels()) {
            for (ModelField field : m.getFields()) {
                System.out.print(m.getName() + "." + field.getName() + ": [");
                for (Space writer : field.getWriters()) {
                    System.out.print(writer.getName() + ", ");
                }
                System.out.println("]");
            }
        }
    }

    private void runModelOperations() {
        for (Space s : app.getSpaces()) {
            for (InstantiatedController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    LocalModelOperationAnalysis localAnalysis = new LocalModelOperationAnalysis(event, event.getHandler().getBody());
                    localAnalysis.run();
                    operations.computeIfAbsent(ic, (key) -> new HashMap<>()).putAll(localAnalysis.getOperations());
                }
            }
        }
    }
}
