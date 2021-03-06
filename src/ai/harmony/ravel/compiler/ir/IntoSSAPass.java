package ai.harmony.ravel.compiler.ir;

import ai.harmony.ravel.compiler.ir.typed.*;

import java.util.*;

/**
 * Convert the IR into SSA form, renaming the registers and
 * adding any necessary phi nodes
 *
 * To convert into SSA, we first compute reaching definitions
 * for each variable at the start of each block.
 * Then we look at the start of each block, and for each
 * variable we look at how many definitions reach it. If more
 * than one, we create a phi node pointing to all the definitions we seen.
 * This creates a new definition of the variable, so the process continues
 * until convergence.
 * (The two steps are actually merged into one because it's easier)
 *
 * Finally, we go through all the phi nodes we created and add
 * real TPhi instructions at the start of the corresponding blocks,
 * allocate registers and rename usages
 *
 * Created by gcampagn on 2/8/17.
 */
class IntoSSAPass {
    private static class Definition {
        TBlock inBlock;

        // only one of instruction or phi node is not null
        TInstruction instruction;
        PhiNode phiNode;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Definition that = (Definition) o;

            if (!inBlock.equals(that.inBlock)) return false;
            if (instruction != null ? !instruction.equals(that.instruction) : that.instruction != null) return false;
            return phiNode != null ? phiNode.equals(that.phiNode) : that.phiNode == null;
        }

        @Override
        public int hashCode() {
            int result = inBlock.hashCode();
            result = 31 * result + (instruction != null ? instruction.hashCode() : 0);
            result = 31 * result + (phiNode != null ? phiNode.hashCode() : 0);
            return result;
        }
    }
    private static class PhiNode {
        TBlock inBlock;
        final Set<Definition> sources = new HashSet<>();
        int variable;

    }

    private final TypedIR ir;
    private boolean progress = false;

    // the dataflow state
    private final Map<TBlock, Map<Integer, Set<Definition>>> atBlockStart = new HashMap<>();
    private final Map<TBlock, Map<Integer, Set<Definition>>> atBlockEnd = new HashMap<>();
    private final Map<TBlock, Map<Integer, PhiNode>> phiNodes = new HashMap<>();

    // a map from all phi nodes to their corresponding definition
    private final Map<PhiNode, Definition> phiDefinitions = new HashMap<>();
    // the global renames we have applied for the sinks of instructions
    private final Map<Definition, Integer> allocatedRegisters = new HashMap<>();
    // the set of all affected variables (ie, variables with more than one definitions)
    // variables that are not in this set will not be transformed into SSA registers
    private final Set<Integer> affectedVars = new HashSet<>();

    private final Map<TBlock, Map<Integer, Integer>> renamesAtBlockEnd = new HashMap<>();

    IntoSSAPass(TypedIR ir) {
        this.ir = ir;
    }

    public void run() {
        dataflowPass();
        convertPhiNodesToPhiInstr();

        // remove all variables that have been renamed from the ir
        for (int var : affectedVars)
            ir.deleteRegister(var);
    }

    private void dataflowPass() {
        do {
            progress = false;
            ir.getControlFlowGraph().visitForward(this::visitBlockDataflow);
        } while(progress);
    }

    // Redefine (assign to) a variable. This drops all the reaching definitions for the variable
    private void define(Map<Integer, Set<Definition>> localState, TBlock inBlock, TInstruction instr, int var) {
        Definition def = new Definition();
        def.inBlock = inBlock;
        def.instruction = instr;

        localState.compute(var, (existingKey, existingDefs) -> {
            Set<Definition> newDefs = new HashSet<>();
            newDefs.add(def);

            if (existingDefs != null && newDefs.equals(existingDefs))
                return existingDefs;
            if (existingDefs != null && !newDefs.equals(existingDefs)) {
                // mark that this var has multiple definitions (
                affectedVars.add(var);
            }

            // drop whatever was existing
            return newDefs;
        });
    }
    private void define(Map<Integer, Set<Definition>> localState, PhiNode phi) {
        Definition def = new Definition();
        def.inBlock = phi.inBlock;
        def.phiNode = phi;
        // drop all r
        localState.put(phi.variable, new HashSet<>());
        localState.get(phi.variable).add(def);

        for (Definition sourceDef : phi.sources)
            assert !def.equals(sourceDef);

        phiDefinitions.put(phi, def);
    }

    private static void meetReachingDefs(Map<Integer, Set<Definition>> localState, Map<Integer, Set<Definition>> fromState) {
        fromState.forEach((var, defs) -> {
            if (localState.containsKey(var))
                localState.get(var).addAll(defs);
            else
                localState.put(var, new HashSet<>(defs)); // always create a copy so we don't worry about mutation
        });
    }

    private static <K, V> Map<K, Set<V>> deepCopy(Map<K, Set<V>> map) {
        Map<K, Set<V>> newMap = new HashMap<>();

        map.forEach((key, value) -> {
            newMap.put(key, new HashSet<>(value));
        });

        return newMap;
    }

    private PhiNode findExisting(TBlock block, int var) {
        return phiNodes.getOrDefault(block, Collections.emptyMap()).get(var);
    }

    private void addPhiNode(TBlock block, int var, PhiNode node) {
        phiNodes.computeIfAbsent(block, (key) -> new HashMap<>()).put(var, node);
    }

    private static Set<Definition> computeEffectiveDefinitions(Set<Definition> defs) {
        if (defs.size() <= 1)
            return defs;

        Set<Definition> result = new HashSet<>();
        Set<Definition> subsumed = new HashSet<>();

        for (Definition def : defs) {
            if (def.instruction == null) {
                for (Definition phiSource : def.phiNode.sources) {
                    result.remove(phiSource);
                    subsumed.add(phiSource);
                }
            }

            if (subsumed.contains(def))
                continue;
            result.add(def);
        }

        return result;
    }

    private void visitBlockDataflow(TBlock block) {
        // Compute the state at the start of the block
        Map<Integer, Set<Definition>> localState = new HashMap<>();
        for (TBlock predecessor : block.getPredecessors()) {
            meetReachingDefs(localState, atBlockEnd.getOrDefault(predecessor, Collections.emptyMap()));
        }
        if (!localState.equals(atBlockStart.getOrDefault(block, Collections.emptyMap())))
            progress = true;
        atBlockStart.put(block, deepCopy(localState));

        // Compute any new phi nodes that we need
        Set<PhiNode> newPhiNodes = new HashSet<>();
        for (int var : localState.keySet()) {
            Set<Definition> defs = computeEffectiveDefinitions(localState.get(var));
            if (defs.size() > 1) {
                // mark that this var has a phi node, at some point
                affectedVars.add(var);

                // we must create a phi node for var at the start of this block

                // first check if we have a phi node for this variable in this block
                // already
                // if we have it, we must use it, which will ensure we don't keep
                // creating new definitions over and over again (otherwise we would
                // never converge!)
                PhiNode existing = findExisting(block, var);

                if (existing != null) {
                    // if we have it, we throw away all the definitions and replace them
                    // with our new definitions
                    if (!existing.sources.equals(defs)) {
                        progress = true;
                        existing.sources.clear();
                        existing.sources.addAll(defs);
                    }
                    newPhiNodes.add(existing);
                } else {
                    // if we don't have it, we make a new phi one, and create a definition
                    // out of it
                    PhiNode phi = new PhiNode();
                    phi.inBlock = block;
                    phi.variable = var;
                    phi.sources.addAll(defs);
                    addPhiNode(block, var, phi);
                    newPhiNodes.add(phi);
                }
            }
        }
        for (PhiNode phi : newPhiNodes)
            define(localState, phi);

        for (TInstruction instr : block) {
            int sink = instr.getSink();
            if (sink != Registers.VOID_REG)
                define(localState, block, instr, sink);
        }

        atBlockEnd.put(block, localState);
    }

    private void convertPhiNodesToPhiInstr() {
        ir.getControlFlowGraph().visitForward(this::visitBlockFinalPass);
    }

    private int getDefinitionRegister(Definition def) {
        return allocatedRegisters.computeIfAbsent(def, (key) -> {
            if (key.phiNode != null)
                return ir.allocateRegister(ir.getRegisterType(key.phiNode.variable));
            else if (affectedVars.contains(key.instruction.getSink()))
                return ir.allocateRegister(key.instruction.getSinkType());
            else
                return key.instruction.getSink();
        });
    }

    private static void meetRenames(Map<Integer, Integer> localState, Map<Integer, Integer> fromState) {
        // there can be multiple renames for a variable, coming from different states
        // but it does not matter because we'll throw that away and make a phi node
        localState.putAll(fromState);
    }

    private int getNewSourceName(Map<Integer, Integer> localState, int src) {
        return localState.getOrDefault(src, src);
    }

    private int getNewSinkName(int sink, TBlock block, TInstruction instr) {
        if (!affectedVars.contains(sink))
            return sink;

        Definition def = new Definition();
        def.inBlock = block;
        def.instruction = instr;
        return getDefinitionRegister(def);
    }

    private void visitBlockFinalPass(TBlock block) {
        Map<Integer, Integer> localState = new HashMap<>();
        for (TBlock predecessor : block.getPredecessors()) {
            meetRenames(localState, renamesAtBlockEnd.getOrDefault(predecessor, Collections.emptyMap()));
        }

        for (Map.Entry<Integer, PhiNode> phi : phiNodes.getOrDefault(block, Collections.emptyMap()).entrySet()) {
            assert phi.getKey() == phi.getValue().variable;
            assert phi.getValue().inBlock == block;
            assert affectedVars.contains(phi.getKey());

            Set<Definition> defs = phi.getValue().sources;
            int[] phiSources = new int[defs.size()];
            TBlock[] phiBlocks = new TBlock[defs.size()];

            int i = 0;
            for (Definition def : defs) {
                phiSources[i] = getDefinitionRegister(def);
                assert Registers.isNormal(phiSources[i]);
                phiBlocks[i] = def.inBlock;
                assert phiBlocks[i] != null;
                assert phiSources[i] != phi.getKey();
                i++;
            }

            Definition phiDefinition = phiDefinitions.get(phi.getValue());
            int newReg = getDefinitionRegister(phiDefinition);
            for (int source : phiSources)
                assert source != newReg;
            block.prepend(new TPhi(ir.getRegisterType(phi.getKey()), newReg, phiSources, phiBlocks));
            localState.put(phi.getKey(), newReg);
        }

        for (TInstruction instr : block) {
            instr.accept(new BaseTInstructionVisitor() {
                @Override
                public void visit(TPhi phi) {
                    // ignore phis, we just created them so we know they're good
                }

                @Override
                public void visit(TArrayLoad arrayLoad) {
                    arrayLoad.source = getNewSourceName(localState, arrayLoad.source);
                    arrayLoad.index = getNewSourceName(localState, arrayLoad.index);
                    int newTarget = getNewSinkName(arrayLoad.target, block, arrayLoad);
                    if (newTarget != arrayLoad.target)
                        localState.put(arrayLoad.target, newTarget);
                    arrayLoad.target = newTarget;
                }

                @Override
                public void visit(TArrayStore arrayStore) {
                    arrayStore.object = getNewSourceName(localState, arrayStore.object);
                    arrayStore.index = getNewSourceName(localState, arrayStore.index);
                    arrayStore.value = getNewSourceName(localState, arrayStore.value);
                }

                @Override
                public void visit(TBinaryArithOp arithOp) {
                    arithOp.src1 = getNewSourceName(localState, arithOp.src1);
                    arithOp.src2 = getNewSourceName(localState, arithOp.src2);
                    int newTarget = getNewSinkName(arithOp.target, block, arithOp);
                    if (newTarget != arithOp.target)
                        localState.put(arithOp.target, newTarget);
                    arithOp.target = newTarget;
                }

                @Override
                public void visit(TComparisonOp compOp) {
                    compOp.src1 = getNewSourceName(localState, compOp.src1);
                    compOp.src2 = getNewSourceName(localState, compOp.src2);
                    int newTarget = getNewSinkName(compOp.target, block, compOp);
                    if (newTarget != compOp.target)
                        localState.put(compOp.target, newTarget);
                    compOp.target = newTarget;
                }

                @Override
                public void visit(TConvert convert) {
                    convert.source = getNewSourceName(localState, convert.source);
                    int newTarget = getNewSinkName(convert.target, block, convert);
                    if (newTarget != convert.target)
                        localState.put(convert.target, newTarget);
                    convert.target = newTarget;
                }

                @Override
                public void visit(TFieldLoad fieldLoad) {
                    fieldLoad.source = getNewSourceName(localState, fieldLoad.source);
                    int newTarget = getNewSinkName(fieldLoad.target, block, fieldLoad);
                    if (newTarget != fieldLoad.target)
                        localState.put(fieldLoad.target, newTarget);
                    fieldLoad.target = newTarget;
                }

                @Override
                public void visit(TFieldStore fieldStore) {
                    fieldStore.object = getNewSourceName(localState, fieldStore.object);
                    fieldStore.value = getNewSourceName(localState, fieldStore.value);
                }

                @Override
                public void visit(TIfStatement ifStatement) {
                    ifStatement.cond = getNewSourceName(localState, ifStatement.cond);
                }

                @Override
                public void visit(TImmediateLoad immediateLoad) {
                    int newTarget = getNewSinkName(immediateLoad.target, block, immediateLoad);
                    if (newTarget != immediateLoad.target)
                        localState.put(immediateLoad.target, newTarget);
                    immediateLoad.target = newTarget;
                }

                @Override
                public void visit(TMethodCall methodCall) {
                    int[] arguments = methodCall.arguments;
                    for (int i = 0; i < arguments.length; i++) {
                        arguments[i] = getNewSourceName(localState, arguments[i]);
                    }
                    if (methodCall.owner != Registers.VOID_REG)
                        methodCall.owner = getNewSourceName(localState, methodCall.owner);
                    if (methodCall.target != Registers.VOID_REG) {
                        int newTarget = getNewSinkName(methodCall.target, block, methodCall);
                        if (newTarget != methodCall.target)
                            localState.put(methodCall.target, newTarget);
                        methodCall.target = newTarget;
                    }
                }

                @Override
                public void visit(TMove move) {
                    move.source = getNewSourceName(localState, move.source);
                    int newTarget = getNewSinkName(move.target, block, move);
                    if (newTarget != move.target)
                        localState.put(move.target, newTarget);
                    move.target = newTarget;
                }

                @Override
                public void visit(TUnaryArithOp arithOp) {
                    arithOp.source = getNewSourceName(localState, arithOp.source);
                    int newTarget = getNewSinkName(arithOp.target, block, arithOp);
                    if (newTarget != arithOp.target)
                        localState.put(arithOp.target, newTarget);
                    arithOp.target = newTarget;
                }

                @Override
                public void visit(TIntrinsic intrinsic) {
                    int[] arguments = intrinsic.arguments;
                    for (int i = 0; i < arguments.length; i++) {
                        arguments[i] = getNewSourceName(localState, arguments[i]);
                    }
                    if (intrinsic.target != Registers.VOID_REG) {
                        int newTarget = getNewSinkName(intrinsic.target, block, intrinsic);
                        if (newTarget != intrinsic.target)
                            localState.put(intrinsic.target, newTarget);
                        intrinsic.target = newTarget;
                    }
                }
            });
        }

        renamesAtBlockEnd.put(block, localState);
    }
}
