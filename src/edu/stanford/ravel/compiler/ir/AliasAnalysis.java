package edu.stanford.ravel.compiler.ir;

import edu.stanford.ravel.compiler.ir.typed.*;
import edu.stanford.ravel.compiler.types.FunctionType;
import edu.stanford.ravel.compiler.types.ModelType;
import edu.stanford.ravel.compiler.types.Type;

import java.util.*;

/**
 * Compute an approximate may-alias relation for record type variables
 *
 * We say that two record variables x and y may-alias if there is a way for them to
 * point to the same record.
 *
 * The may-alias relation is symmetric but not transitive, eg in the case
 * r1 = m.create()
 * r2 = m.create()
 * m.save(r1)
 * m.save(r2)
 * r3 = m.get(...)
 *
 * r1 may-alias r3, r2 may-alias r3, but r1 cannot alias r2
 *
 * This means that, despite SSA, we need to keep the data flow pass per-program-point
 *
 * Created by gcampagn on 2/13/17.
 */
public class AliasAnalysis {
    // The set of all record variables passed to a save() for that type,
    // at the beginning of that block
    private final Map<TBlock, Map<ModelType, Set<Integer>>> writtenRecordsAtStart = new HashMap<>();
    // The set of all record variables obtained by a function call other
    // than Model.create
    private final Map<TBlock, Map<ModelType, Set<Integer>>> readRecordsAtStart = new HashMap<>();
    // The may-alias relation at the start of the block
    private final Map<TBlock, Map<Integer, Set<Integer>>> mayAliasAtStart = new HashMap<>();

    // same thing, but at end
    private final Map<TBlock, Map<ModelType, Set<Integer>>> writtenRecordsAtEnd = new HashMap<>();
    private final Map<TBlock, Map<ModelType, Set<Integer>>> readRecordsAtEnd = new HashMap<>();
    // The may-alias relation at the end of the block
    private final Map<TBlock, Map<Integer, Set<Integer>>> mayAliasAtEnd = new HashMap<>();


    private final TypedIR ir;
    private boolean progress;

    public AliasAnalysis(TypedIR ir) {
        this.ir = ir;
    }

    public Map<Integer, Set<Integer>> run() {
        do {
            progress = false;
            runOneStep();
        } while(progress);

        return computeFinalResult();
    }

    private Map<Integer, Set<Integer>> computeFinalResult() {
        Map<Integer, Set<Integer>> result = new HashMap<>();

        mayAliasAtEnd.get(ir.getControlFlowGraph().getExit()).forEach((var, aliases) -> {
            if (!(ir.getRegisterType(var) instanceof ModelType.RecordType))
                return;
            for (int alias : aliases) {
                if (ir.getRegisterType(var).equalsExceptQualifiers(ir.getRegisterType(alias)))
                    result.computeIfAbsent(var, (key) -> new HashSet<>()).add(alias);
            }
        });

        return Collections.unmodifiableMap(result);
    }

    private void tagMayAlias(Map<Integer, Set<Integer>> mayAlias, int v1, int v2) {
        if (v1 == v2)
            return;
        mayAlias.computeIfAbsent(v1, (key) -> new HashSet<>()).add(v2);
        mayAlias.computeIfAbsent(v2, (key) -> new HashSet<>()).add(v1);
    }

    private void runOneStep() {
        ir.getControlFlowGraph().visitForward(this::visitBlock);
    }

    private <E> void meet(Map<E, Set<Integer>> localState, Map<E, Set<Integer>> fromState) {
        fromState.forEach((var, regs) -> {
            if (localState.containsKey(var))
                localState.get(var).addAll(regs);
            else
                localState.put(var, new HashSet<>(regs)); // always create a copy so we don't worry about mutation
        });
    }

    private static <K, V> Map<K, Set<V>> deepCopy(Map<K, Set<V>> map) {
        Map<K, Set<V>> newMap = new HashMap<>();

        map.forEach((key, value) -> {
            newMap.put(key, new HashSet<>(value));
        });

        return newMap;
    }

    private void aliasReadModel(Map<ModelType, Set<Integer>> localWrittenRecords,
                                Map<ModelType, Set<Integer>> localReadRecords,
                                Map<Integer, Set<Integer>> localMayAlias,
                                ModelType modelType,
                                int var) {
        for (int readRecord : localReadRecords.getOrDefault(modelType, Collections.emptySet()))
            tagMayAlias(localMayAlias, var, readRecord);
        for (int writtenRecord : localWrittenRecords.getOrDefault(modelType, Collections.emptySet()))
            tagMayAlias(localMayAlias, var, writtenRecord);
        localReadRecords.computeIfAbsent(modelType, (key) -> new HashSet<>()).add(var);
    }

    private void handleMove(Map<Integer, Set<Integer>> localMayAlias, int target, int source) {
        // make a copy of the existing aliases of instr.source before we iterate and mutate it
        Set<Integer> existingAliases = new HashSet<>(localMayAlias.getOrDefault(source, Collections.emptySet()));
        for (int alias : existingAliases) {
            tagMayAlias(localMayAlias, target, alias);
        }
        tagMayAlias(localMayAlias, target, source);
    }

    private void visitBlock(TBlock block) {
        // Compute the state at the start of the block
        Map<ModelType, Set<Integer>> localWrittenRecords = new HashMap<>();
        Map<ModelType, Set<Integer>> localReadRecords = new HashMap<>();
        Map<Integer, Set<Integer>> localMayAlias = new HashMap<>();
        for (TBlock predecessor : block.getPredecessors()) {
            meet(localWrittenRecords, writtenRecordsAtEnd.getOrDefault(predecessor, Collections.emptyMap()));
            meet(localReadRecords, readRecordsAtEnd.getOrDefault(predecessor, Collections.emptyMap()));
            meet(localMayAlias, mayAliasAtEnd.getOrDefault(predecessor, Collections.emptyMap()));
        }
        if (!localWrittenRecords.equals(writtenRecordsAtStart.getOrDefault(block, Collections.emptyMap())))
            progress = true;
        if (!localReadRecords.equals(readRecordsAtStart.getOrDefault(block, Collections.emptyMap())))
            progress = true;
        if (!localMayAlias.equals(mayAliasAtStart.getOrDefault(block, Collections.emptyMap())))
            progress = true;
        writtenRecordsAtStart.put(block, deepCopy(localWrittenRecords));
        readRecordsAtStart.put(block, deepCopy(localReadRecords));
        mayAliasAtStart.put(block, deepCopy(localMayAlias));
        //TODO: another model call pass
        for (TInstruction instr : block) {
            if (instr instanceof TMethodCall) {
                FunctionType type = ((TMethodCall) instr).type;
                if (type.getOwner() instanceof ModelType) {
                    ModelType modelType = (ModelType) type.getOwner();
                    switch (type.getFunctionName()) {
                        case "create":
                            // brand new record, aliases nothing
                            break;

                        case "get":
                        case "first":
                        case "last":
                            // aliases any read record and any written record up to here
                            aliasReadModel(localWrittenRecords, localReadRecords, localMayAlias, modelType, instr.getSink());
                            break;

                        case "save":
                            // mark that this record went into the model (so a successive call to .get() may-alias it)
                            localWrittenRecords.computeIfAbsent(modelType, (key) -> new HashSet<>()).add(instr.getSources()[0]);
                            break;

                        case "iterator":
                        case "size":
                        case "clear":
                        case "clearAll":
                            // no pointers, no alias
                            break;

                        case "delete":
                            // does not return a pointer, so no new alias
                            break;

                        default:
                            throw new AssertionError("Invalid model function " + type.getFunctionName());
                    }
                } else if (type.getOwner() instanceof ModelType.IteratorType) {
                    switch (type.getFunctionName()) {
                        case "hasNext":
                            // no pointers, no alias
                            break;

                        case "next":
                            // aliases any read record and any written record up to here
                            aliasReadModel(localWrittenRecords, localReadRecords, localMayAlias, ((ModelType.IteratorType)type.getOwner()).getOwner(), instr.getSink());
                            break;

                        default:
                            throw new AssertionError("Invalid model iterator " + type.getFunctionName());
                    }
                } else {
                    int[] sources = instr.getSources();
                    Type[] sourceTypes = instr.getSourceTypes();

                    for (int i = 0; i < sources.length; i++) {
                        int src = sources[i];
                        if (sourceTypes[i] instanceof ModelType.RecordType) {
                            // behave like .save()
                            localWrittenRecords.computeIfAbsent(((ModelType.RecordType)sourceTypes[i]).getModel(), (key) -> new HashSet<>()).add(src);
                        }
                    }

                    if (instr.getSinkType() instanceof ModelType.RecordType) {
                        // behave like a call to .get()/.all()
                        ModelType modelType = ((ModelType.RecordType) instr.getSinkType()).getModel();
                        aliasReadModel(localWrittenRecords, localReadRecords, localMayAlias, modelType, instr.getSink());
                    }
                }
            } else if (instr instanceof TFieldStore) {
                // if fields can be full records, then we have to track aliases across fields, and this
                // becomes either type analysis (imprecise) or full blown pointer analysis (slow and messy)
                //
                // just assert that fields cannot be records, and move on
                //
                // note: this relies on the fact that self.record is not writable for ContextType
                assert !(((TFieldStore) instr).type instanceof ModelType.RecordType);
            } else if (instr instanceof TFieldLoad) {
                TFieldLoad fieldLoad = (TFieldLoad) instr;
                if (fieldLoad.compoundType instanceof ModelType.ContextType && fieldLoad.field.equals("record")) {
                    ModelType modelType = ((ModelType.ContextType)fieldLoad.compoundType).getOwner();
                    aliasReadModel(localWrittenRecords, localReadRecords, localMayAlias, modelType, instr.getSink());
                } else {
                    assert !(((TFieldLoad) instr).type instanceof ModelType.RecordType);
                }
            } else if (instr instanceof TArrayLoad) {
                // unfortunately, arrays of records do exist
                // for now, just pretend that the array aliases with the value (and then after
                // completion refine on type)
                if (((TArrayLoad) instr).type instanceof ModelType.RecordType)
                    tagMayAlias(localMayAlias, ((TArrayLoad) instr).target, ((TArrayLoad) instr).source);
            } else if (instr instanceof TArrayStore) {
                if (((TArrayStore) instr).type instanceof ModelType.RecordType)
                    tagMayAlias(localMayAlias, ((TArrayStore) instr).object, ((TArrayStore) instr).value);
            } else if (instr instanceof TMove) {
                TMove move = (TMove) instr;
                if (move.type instanceof ModelType.RecordType) {
                    handleMove(localMayAlias, move.target, move.source);
                }
            } else if (instr instanceof TConvert) {
                TConvert convert = (TConvert) instr;
                if (convert.tgtType instanceof ModelType.RecordType) {
                    // convert Record -> const Record
                    assert convert.srcType instanceof ModelType.RecordType;

                    // treat like a move
                    handleMove(localMayAlias, convert.target, convert.source);
                } else {
                    assert !(convert.srcType instanceof ModelType.RecordType);
                }
            } else if (instr instanceof TPhi) {
                TPhi phi = (TPhi) instr;
                if (phi.type instanceof ModelType.RecordType) {
                    for (int source : phi.sources) {
                        handleMove(localMayAlias, phi.target, source);
                    }
                }
            } else if (instr instanceof TIntrinsic) {
                assert !(instr.getSinkType() instanceof ModelType.RecordType);
            } else {
                // no other instruction can involve records
                for (Type srcType : instr.getSourceTypes())
                    assert !(srcType instanceof ModelType.RecordType);
                assert !(instr.getSinkType() instanceof ModelType.RecordType);
            }
        }

        writtenRecordsAtEnd.put(block, localWrittenRecords);
        readRecordsAtEnd.put(block, localReadRecords);
        mayAliasAtEnd.put(block, localMayAlias);
    }
}
