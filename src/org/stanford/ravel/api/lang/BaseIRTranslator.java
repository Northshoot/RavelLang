package org.stanford.ravel.api.lang;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gcampagn on 1/25/17.
 */
public abstract class BaseIRTranslator implements IRTranslator, LoopTreeVisitor, TInstructionVisitor {
    private StringBuilder builder;
    private Map<Integer, String> registerNames = new HashMap<>();

    protected BaseIRTranslator() {
        builder = new StringBuilder();
    }

    protected void setRegisterName(int reg, String name) {
        registerNames.put(reg, name);
    }
    protected String getRegisterName(int reg) {
        String name = registerNames.get(reg);
        if (name == null)
            return "t_" + reg;
        else
            return name;
    }

    protected void addCode(String code) {
        builder.append(code);
    }

    protected void declareControllerScope(VariableSymbol sym) {
        setRegisterName(sym.getRegister(), sym.getName());
    }

    protected void declareEventScope(VariableSymbol sym) {
        setRegisterName(sym.getRegister(), sym.getName());
    }

    protected abstract void declareRegister(int reg, Type type);

    @Override
    public void translate(List<VariableSymbol> controllerParams, List<VariableSymbol> eventParams, TypedIR ir) {
        registerNames.clear();
        builder.setLength(0);

        // declare all the controller parameters
        for (VariableSymbol sym : controllerParams)
            declareControllerScope(sym);
        // declare all event parameters
        for (VariableSymbol sym : eventParams)
            declareEventScope(sym);

        // declare all unnamed (temporary, gp) registers
        // (this includes also named variables in the original code,
        // because we don't bother tracking them)
        for (Map.Entry<Integer, Type> entry : ir.getRegisterTypes()) {
            if (entry.getValue() == PrimitiveType.VOID)
                continue;
            if (registerNames.containsKey(entry.getKey()))
                continue;
            declareRegister(entry.getKey(), entry.getValue());
        }

        ir.getLoopTree().accept(this);
    }

    @Override
    public void visit(LoopTreeNode.BasicBlock block) {
        for (TInstruction instr : block.getBlock()) {
            instr.accept(this);
        }
    }

    // default impl for if statement IR ops does nothing
    // (if statements are handled as control flow by the loop tree translation)
    @Override
    public void visit(TIfStatement ifStatement) {

    }

    @Override
    public String getCode() {
        return builder.toString();
    }
}
