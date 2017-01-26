package org.stanford.ravel.compiler.backend;

import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.ir.untyped.InstructionVisitor;
import org.stanford.ravel.compiler.types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/25/17.
 */
public abstract class BaseTranslator implements Translator, LoopTreeVisitor, TInstructionVisitor {
    private StringBuilder builder;
    private Map<Integer, String> registerNames = new HashMap<>();

    BaseTranslator() {
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

    @Override
    public void translate(TypedIR ir) {
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
