package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.stanford.ravel.compiler.ir.Registers.UNSET_REG;

/**
 * Created by gcampagn on 1/24/17.
 */
public class TypedIR {
    private final Map<Integer, Type> registerTypes = new HashMap<>();
    private ControlFlowGraph cfg;
    private LoopTreeNode loopTree;

    public ControlFlowGraph getControlFlowGraph() {
        return cfg;
    }
    public LoopTreeNode getLoopTree() {
        return loopTree;
    }
    public void finish(ControlFlowGraphBuilder cfg, LoopTreeBuilder loopTreeBuilder) {
        this.cfg = cfg.build();
        this.loopTree = loopTreeBuilder.getRoot();
    }

    public void setRegisterType(int reg, Type type) {
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

    public Collection<Map.Entry<Integer, Type>> getRegisterTypes() {
        return registerTypes.entrySet();
    }
}
