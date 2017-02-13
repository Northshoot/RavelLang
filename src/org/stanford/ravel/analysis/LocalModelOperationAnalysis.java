package org.stanford.ravel.analysis;

import org.stanford.ravel.compiler.ir.BinaryOperation;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.types.FunctionType;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.primitives.LinkedEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The local (single function) version of ModelOperationAnalysis
 *
 * Created by gcampagn on 2/13/17.
 */
public class LocalModelOperationAnalysis {
    private final Map<FieldTag, Operation> operations = new HashMap<>();
    private final LinkedEvent event;
    private final TypedIR ir;
    private boolean progress;

    public LocalModelOperationAnalysis(LinkedEvent event, TypedIR ir) {
        this.event = event;
        this.ir = ir;
    }

    public Map<FieldTag, Operation> getOperations() {
        return Collections.unmodifiableMap(operations);
    }

    public void run() {
        do {
            progress = false;
            runOneStep();
        } while(progress);
    }

    private void runOneStep() {
        ir.getControlFlowGraph().visitBackward(this::visitBlock);
    }

    private void meetOperation(FieldTag field, Operation op) {
        operations.compute(field, (key, existingOp) -> {
            Operation result = Operation.meet(existingOp, op);
            if (result != existingOp)
                progress = true;
            return result;
        });
    }

    private void meetOperation(int variable, Operation op) {
        // if the operation is on a record, it's applied to all fields
        for (FieldTag fieldTag : event.getVariableFieldTags(variable)) {
            meetOperation(fieldTag, op);
        }
    }

    private void handleMethodCall(LinkedEvent event, TMethodCall instr) {
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
                    meetOperation(instr.getSources()[0], Operation.ANY);
                    break;

                case "save":
                    // save is just a fancy move, but we're moving all fields
                    ModelType.RecordType recordType = (ModelType.RecordType)instr.getSourceTypes()[0];
                    int record = instr.getSources()[0];
                    for (String fieldName : recordType.getMemberList()) {
                        for (ModelTag modelTag : event.getVariableModelTags(record)) {
                            meetOperation(new FieldTag(modelTag.model, modelTag.creator, fieldName), Operation.MOVE);
                        }
                    }
                    break;

                default:
                    throw new AssertionError("Unexpected model function " + type.getFunctionName());
            }
        } else {
            // interface function, here be dragons, anything can happen
            for (int source : instr.getSources()) {
                meetOperation(source, Operation.ANY);
            }
        }
    }

    private void visitBlock(TBlock block) {
        for (TInstruction instr : block) {
            if (instr instanceof TMethodCall) {
                handleMethodCall(event, (TMethodCall) instr);
            } else if (instr instanceof TArrayLoad) {
                meetOperation(((TArrayLoad) instr).index, Operation.ANY);
                meetOperation(((TArrayLoad) instr).source, Operation.INDEX_LOAD);
            } else if (instr instanceof TArrayStore) {
                meetOperation(((TArrayStore) instr).index, Operation.ANY);
                meetOperation(((TArrayStore) instr).value, Operation.MOVE);
                meetOperation(((TArrayStore) instr).object, Operation.INDEX_LOAD);
            } else if (instr instanceof TFieldLoad) {
                meetOperation(((TFieldLoad) instr).source, Operation.INDEX_LOAD);
            } else if (instr instanceof TFieldStore) {
                meetOperation(((TFieldStore) instr).value, Operation.MOVE);
                meetOperation(((TFieldStore) instr).object, Operation.INDEX_STORE);
            } else if (instr instanceof TMove) {
                meetOperation(((TMove) instr).source, Operation.MOVE);
            } else if (instr instanceof TBinaryArithOp) {
                if (((TBinaryArithOp) instr).op == BinaryOperation.ADD) {
                    if (((TBinaryArithOp) instr).type == PrimitiveType.STR) {
                        for (int source : instr.getSources())
                            meetOperation(source, Operation.CONCAT);
                    }
                } else {
                    // FIXME handle add constant
                    int src1 = ((TBinaryArithOp) instr).src1;
                    int src2 = ((TBinaryArithOp) instr).src2;
                    if (event.getVariableModelTags(src1).equals(event.getVariableModelTags(src2))) {
                        meetOperation(src1, Operation.ADD_SAME);
                        meetOperation(src2, Operation.ADD_SAME);
                    } else {
                        meetOperation(src1, Operation.ADD_FOREIGN);
                        meetOperation(src2, Operation.ADD_FOREIGN);
                    }
                }
            } else if (instr instanceof TPhi) {
                // a phi is nothing but a fancy move
                meetOperation(instr.getSink(), Operation.MOVE);

                // propagate back from the sink to the sources
                for (FieldTag fieldTag : event.getVariableFieldTags(instr.getSink())) {
                    for (int source : instr.getSources()) {
                        meetOperation(source, operations.get(fieldTag));
                    }
                }
            } else {
                // anything else is here-be-dragons, anything can happen, need full decryption
                for (int source : instr.getSources()) {
                    meetOperation(source, Operation.ANY);
                }
            }
        }
    }
}
