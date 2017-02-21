package org.stanford.ravel.compiler.ir.typed;

import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcampagn on 2/21/17.
 */
public class TypedIRBuilder {
    private final LoopTreeBuilder loopTreeBuilder = new LoopTreeBuilder();
    private final ControlFlowGraphBuilder cfgBuilder = new ControlFlowGraphBuilder();
    private final TypedIR ir = new TypedIR();

    private final List<TBlock> loopHeads = new ArrayList<>();
    private final List<TBlock> loopContinuations = new ArrayList<>();

    public TypedIRBuilder() {
        loopTreeBuilder.addBasicBlock(cfgBuilder.getEntry());
    }

    public ControlFlowGraphBuilder getControlFlowGraphBuilder() {
        return cfgBuilder;
    }
    public LoopTreeBuilder getLoopTreeBuilder() {
        return loopTreeBuilder;
    }

    public void setNextRegister(int firstGpRegister) {
        ir.setNextRegister(firstGpRegister);
    }

    public void setRegisterType(int reg, Type type) {
        ir.setRegisterType(reg, type);
    }
    public Type getRegisterType(int reg) {
        return ir.getRegisterType(reg);
    }
    public int allocateRegister(Type type) {
        return ir.allocateRegister(type);
    }
    public void declareParameter(VariableSymbol sym, boolean classScope) {
        ir.declareParameter(sym, classScope);
    }

    public TypedIR finish() {
        ir.finish(cfgBuilder, loopTreeBuilder);
        return ir;
    }

    public void addInstruction(TInstruction instr) {
        cfgBuilder.addInstruction(instr);
    }
}
