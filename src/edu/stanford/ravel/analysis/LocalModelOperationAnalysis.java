package edu.stanford.ravel.analysis;

import edu.stanford.ravel.compiler.ir.typed.*;
import edu.stanford.ravel.compiler.types.ModelType;
import edu.stanford.ravel.primitives.LinkedEvent;
import edu.stanford.ravel.compiler.ir.BinaryOperation;
import edu.stanford.ravel.compiler.types.FunctionType;
import edu.stanford.ravel.compiler.types.PrimitiveType;

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
            //TODO: not sure this is the best way to handle call, har do add new methods
            switch (type.getFunctionName()) {
                case "create":
                case "iterator":
                case "last":
                case "clearAll":
                    assert instr.getSources().length == 1;
                    // all these have no sources, so no operations to meet
                    break;

                case "get":
                case "clear":
                case "size":
                case "first":
                    // the argument to get is an index, which must be fully decrypted
                    meetOperation(instr.getSources()[1], Operation.ANY);
                    break;
                case "save":
                    // save is just a fancy move, but we're moving all fields
                    ModelType.RecordType recordType = (ModelType.RecordType) instr.getSourceTypes()[1];
                    int record = instr.getSources()[1];
                    /*for (String fieldName : recordType.getMemberList()) {
                        for (ModelTag modelTag : event.getVariableModelTags(record)) {
                            assert modelTag.model != null;
                            if (modelTag.model.getType().getRecordType().equalsExceptQualifiers(recordType)) {
                                meetOperation(new FieldTag(modelTag.model, modelTag.creator, fieldName), Operation.MOVE);
                            }
                        }
                    }*/
                    for (FieldTag fieldTag : event.getVariableFieldTags(record)) {
                        meetOperation(fieldTag, Operation.MOVE);
                    }
                    break;

                case "delete":
                    // delete does nothing and deletes local storage (which can be encrypted for all we care)
                    break;

                default:
                    throw new AssertionError("Unexpected model function " + type.getFunctionName());
            }
        } else if (type.getOwner() instanceof ModelType.IteratorType) {
            switch (type.getFunctionName()) {
                case "hasNext":
                case "next":
                    assert instr.getSources().length == 1;
                    // all these have no sources, so no operations to meet
                    break;
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
                meetOperation(((TArrayStore) instr).object, Operation.INDEX_STORE);
            } else if (instr instanceof TFieldLoad) {
                if (!(((TFieldLoad) instr).compoundType instanceof ModelType.RecordType))
                    meetOperation(((TFieldLoad) instr).source, Operation.INDEX_LOAD);
            } else if (instr instanceof TFieldStore) {
                meetOperation(((TFieldStore) instr).value, Operation.MOVE);
                if (!(((TFieldStore) instr).compoundType instanceof ModelType.RecordType))
                    meetOperation(((TFieldStore) instr).object, Operation.INDEX_STORE);
            } else if (instr instanceof TMove) {
                meetOperation(((TMove) instr).source, Operation.MOVE);
            } else if (instr instanceof TBinaryArithOp) {
                // str + str => concat
                // int32 + int32 => iadd
                // int32 * int32 => imul
                // anything else => any

                if (((TBinaryArithOp) instr).op == BinaryOperation.ADD) {
                    if (((TBinaryArithOp) instr).type == PrimitiveType.STR) {
                        for (int source : instr.getSources())
                            meetOperation(source, Operation.CONCAT);
                    } else if (((TBinaryArithOp) instr).type == PrimitiveType.INT32) {
                        for (int source : instr.getSources())
                            meetOperation(source, Operation.IADD);
                    } else {
                        for (int source : instr.getSources())
                            meetOperation(source, Operation.ANY);
                    }
                } else if (((TBinaryArithOp) instr).op == BinaryOperation.MUL) {
                    if (((TBinaryArithOp) instr).type == PrimitiveType.INT32) {
                        for (int source : instr.getSources())
                            meetOperation(source, Operation.IMUL);
                    } else {
                        for (int source : instr.getSources())
                            meetOperation(source, Operation.ANY);
                    }
                } else {
                    // anything else is here-be-dragons, anything can happen, need full decryption
                    for (int source : instr.getSources()) {
                        meetOperation(source, Operation.ANY);
                    }
                }
            } else if (instr instanceof TPhi) {
                // propagate back from the sink to the sources
                for (FieldTag fieldTag : event.getVariableFieldTags(instr.getSink())) {
                    for (int source : instr.getSources()) {
                        meetOperation(source, operations.get(fieldTag));
                    }
                }
            } else if (instr instanceof TConvert) {
                if (((TConvert) instr).srcType.equalsExceptQualifiers(((TConvert) instr).tgtType)) {
                    // if the types are actually the same, this is just a move
                    meetOperation(((TConvert) instr).source, Operation.MOVE);
                } else {
                    // otherwise, this requires the full plaintext representation of the input
                    // (eg, it's a sign extension, or a conversion between double and int)
                    meetOperation(((TConvert) instr).source, Operation.ANY);
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
