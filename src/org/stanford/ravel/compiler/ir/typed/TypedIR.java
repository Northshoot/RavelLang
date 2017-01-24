package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

import java.util.HashMap;
import java.util.Map;

import static org.stanford.ravel.compiler.ir.Registers.UNSET_REG;

/**
 * Created by gcampagn on 1/24/17.
 */
public class TypedIR {
    private final Map<Integer, Type> registerTypes = new HashMap<>();
    private ControlFlowGraph cfg;

    public ControlFlowGraph getControlFlowGraph() {
        return cfg;
    }
    public void finish(ControlFlowGraphBuilder cfg) {
        this.cfg = cfg.build();
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
}
