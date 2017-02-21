package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.ClassType;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

import java.util.*;

import static org.stanford.ravel.compiler.ir.Registers.UNSET_REG;

/**
 * Created by gcampagn on 1/24/17.
 */
public class TypedIR {
    private final Map<Integer, Type> registerTypes = new HashMap<>();
    private final Set<Integer> parameters = new HashSet<>();
    private final Set<VariableSymbol> classVariables = new HashSet<>();
    private final Set<VariableSymbol> argumentVariables = new HashSet<>();

    private ControlFlowGraph cfg;
    private LoopTreeNode loopTree;
    private int nextRegister = UNSET_REG;
    private Map<Integer, Set<Integer>> aliases;

    public ControlFlowGraph getControlFlowGraph() {
        return cfg;
    }
    public LoopTreeNode getLoopTree() {
        return loopTree;
    }

    void finish(ControlFlowGraphBuilder cfg, LoopTreeBuilder loopTreeBuilder) {
        this.cfg = cfg.build();
        loopTreeBuilder.addBasicBlock(cfg.getExit());
        this.loopTree = loopTreeBuilder.getRoot();
    }
    void setRegisterType(int reg, Type type) {
        assert Registers.isNormal(reg);
        assert !registerTypes.containsKey(reg);
        registerTypes.put(reg, type);
    }
    public Type getRegisterType(int reg) {
        if (reg == Registers.VOID_REG)
            return PrimitiveType.VOID;
        if (reg == Registers.ERROR_REG)
            return PrimitiveType.ERROR;
        Type type = registerTypes.get(reg);
        if (type == null)
            return PrimitiveType.ANY;
        else
            return type;
    }

    void setNextRegister(int firstGpRegister) {
        nextRegister = firstGpRegister;
    }
    public int allocateRegister(Type type) {
        assert type != PrimitiveType.VOID;

        int reg = nextRegister++;
        setRegisterType(reg, type);
        return reg;
    }

    public Collection<Map.Entry<Integer, Type>> getRegisterTypes() {
        return registerTypes.entrySet();
    }

    public void deleteRegister(int var) {
        registerTypes.remove(var);
    }

    public Collection<Integer> getVariables() {
        return registerTypes.keySet();
    }

    public void setAliases(Map<Integer, Set<Integer>> aliases) {
        this.aliases = aliases;
    }

    public Set<Integer> getAliases(int var) {
        return aliases.getOrDefault(var, Collections.emptySet());
    }

    private void addParameter(int param) {
        assert Registers.isNormal(param);
        assert getRegisterType(param) != PrimitiveType.VOID;
        parameters.add(param);
    }

    void declareParameter(VariableSymbol sym, boolean classScope) {
        if (classScope)
            classVariables.add(sym);
        else
            argumentVariables.add(sym);
        addParameter(sym.getRegister());

        Type type = sym.getType();
        if (type instanceof ClassType)
            type = ((ClassType) type).getInstanceType();
        setRegisterType(sym.getRegister(), type);
    }

    public Collection<Integer> getParameters() {
        return Collections.unmodifiableCollection(parameters);
    }

    public Collection<VariableSymbol> getArguments() {
        return Collections.unmodifiableCollection(argumentVariables);
    }
    public Collection<VariableSymbol> getClassScopeVariables() {
        return Collections.unmodifiableCollection(classVariables);
    }
}
