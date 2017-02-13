package org.stanford.ravel.analysis;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.ir.BinaryOperation;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.types.FunctionType;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.InstantiatedController;
import org.stanford.ravel.primitives.LinkedEvent;
import org.stanford.ravel.primitives.Space;

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
        } while(progress);

        if (debug) {
            dumpAllOperations();
        }
    }

    private void dumpAllOperations() {
        System.out.println("Operations on each model field:");
        operations.forEach((ic, icOperations) -> {
            System.out.println("By " + ic);
            icOperations.forEach((field, op) -> {
                System.out.println(field + ": " + op);
            });
            System.out.println();
        });
    }

    private void meetOperation(InstantiatedController ic, FieldTag field, Operation op) {
        operations.computeIfAbsent(ic, (key) -> new HashMap<>()).compute(field, (key, existingOp) -> {
            Operation result = Operation.meet(existingOp, op);
            if (result != existingOp)
                progress = true;
            return result;
        });
    }

    private void meetOperation(InstantiatedController ic, LinkedEvent event, int variable, Type type, Operation op) {
        // if the operation is on a record, it's applied to all fields
        if (type instanceof ModelType.RecordType) {
            for (String fieldName : ((ModelType.RecordType) type).getMemberList()) {
                for (ModelTag modelTag : event.getVariableModelTags(variable)) {
                    meetOperation(ic, new FieldTag(modelTag.model, modelTag.creator, fieldName), op);
                }
            }
        } else {
            for (FieldTag fieldTag : event.getVariableFieldTags(variable)) {
                meetOperation(ic, fieldTag, op);
            }
        }
    }


    private void handleMethodCall(InstantiatedController ic, LinkedEvent event, TMethodCall instr) {
        FunctionType type = instr.type;
        if (type.getOwner() instanceof ModelType) {
            // first the easy case: method calls on a model object
            // we know the semantics of these, we can just encode them directly

            switch (type.getFunctionName()) {
                case "create":
                case "all":
                case "first":
                case "last":
                case "clear":
                    assert instr.getSources().length == 0;
                    // all these have no sources, so no operations to meet
                    break;

                case "get":
                    // the argument to get is an index, which must be fully decrypted
                    meetOperation(ic, event, instr.getSources()[0], instr.getSourceTypes()[0], Operation.ANY);
                    break;

                case "save":
                    // save is just a fancy move
                    meetOperation(ic, event, instr.getSources()[0], instr.getSourceTypes()[0], Operation.MOVE);
                    break;

                default:
                    throw new AssertionError("Unexpected model function " + type.getFunctionName());
            }
        } else {
            // interface function, here be dragons, anything can happen
            int[] sources = instr.getSources();
            Type[] sourceTypes = instr.getSourceTypes();

            for (int i = 0; i < sources.length; i++) {
                meetOperation(ic, event, sources[i], sourceTypes[i], Operation.ANY);
            }
        }
    }

    private void runLocalModelOperations(InstantiatedController ic, LinkedEvent event, TypedIR ir) {
        ir.getControlFlowGraph().visitForward((block) -> {
            for (TInstruction instr : block) {
                if (instr instanceof TMethodCall) {
                    handleMethodCall(ic, event, (TMethodCall)instr);
                } else if (instr instanceof TArrayLoad) {
                    meetOperation(ic, event, ((TArrayLoad) instr).index, PrimitiveType.INT32, Operation.ANY);
                    meetOperation(ic, event, ((TArrayLoad) instr).source, ((TArrayLoad) instr).arrayType, Operation.INDEX_LOAD);
                } else if (instr instanceof TArrayStore) {
                    meetOperation(ic, event, ((TArrayStore) instr).index, PrimitiveType.INT32, Operation.ANY);
                    meetOperation(ic, event, ((TArrayStore) instr).value, ((TArrayStore) instr).type, Operation.MOVE);
                    meetOperation(ic, event, ((TArrayStore) instr).object, ((TArrayStore) instr).arrayType, Operation.INDEX_LOAD);
                } else if (instr instanceof TFieldLoad) {
                    // if we're reading from an user defined object, we need to mark the operation on the object
                    // if we're reading from a record, we have field tags to recognize what is what, so there is nothing to mark
                    if (!(((TFieldLoad) instr).compoundType instanceof ModelType.RecordType))
                        meetOperation(ic, event, ((TFieldLoad) instr).source, ((TFieldLoad) instr).compoundType, Operation.INDEX_LOAD);
                } else if (instr instanceof TFieldStore) {
                    meetOperation(ic, event, ((TFieldStore) instr).value, ((TFieldStore) instr).type, Operation.MOVE);
                    // if we're writing to an user defined object, we need to mark the operation on the object
                    // if we're writing to a record, we have field tags to recognize what is what, so there is nothing to mark
                    if (!(((TFieldStore) instr).compoundType instanceof ModelType.RecordType))
                        meetOperation(ic, event, ((TFieldStore) instr).object, ((TFieldStore) instr).compoundType, Operation.INDEX_STORE);
                } else if (instr instanceof TMove) {
                    // if we're moving a record, we're not actually moving all fields (until we move into save)
                    if (!(((TMove) instr).type instanceof ModelType.RecordType))
                        meetOperation(ic, event, ((TMove) instr).source, ((TMove) instr).type, Operation.MOVE);
                } else if (instr instanceof TBinaryArithOp) {
                    if (((TBinaryArithOp) instr).op == BinaryOperation.ADD) {
                        if (((TBinaryArithOp) instr).type == PrimitiveType.STR) {
                            for (int source : instr.getSources())
                                meetOperation(ic, event, source, PrimitiveType.STR, Operation.CONCAT);
                        }
                    } else {
                        // FIXME handle add constant
                        int src1 = ((TBinaryArithOp) instr).src1;
                        int src2 = ((TBinaryArithOp) instr).src2;
                        if (event.getVariableModelTags(src1).equals(event.getVariableModelTags(src2))) {
                            meetOperation(ic, event, src1, ((TBinaryArithOp) instr).type, Operation.ADD_SAME);
                            meetOperation(ic, event, src2, ((TBinaryArithOp) instr).type, Operation.ADD_SAME);
                        } else {
                            meetOperation(ic, event, src1, ((TBinaryArithOp) instr).type, Operation.ADD_FOREIGN);
                            meetOperation(ic, event, src2, ((TBinaryArithOp) instr).type, Operation.ADD_FOREIGN);
                        }
                    }
                } else {
                    // anything else is here-be-dragons, anything can happen, need full decryption
                    int[] sources = instr.getSources();
                    Type[] sourceTypes = instr.getSourceTypes();

                    for (int i = 0; i < sources.length; i++) {
                        meetOperation(ic, event, sources[i], sourceTypes[i], Operation.ANY);
                    }
                }
            }
        });
    }

    private void runModelOperations() {
        for (Space s : app.getSpaces()) {
            for (InstantiatedController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    runLocalModelOperations(ic, event, event.getHandler().getBody());
                }
            }
        }
    }
}
