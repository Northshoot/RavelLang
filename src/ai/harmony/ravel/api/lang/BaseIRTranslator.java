package ai.harmony.ravel.api.lang;

import ai.harmony.ravel.compiler.ir.Registers;
import ai.harmony.ravel.compiler.ir.typed.*;
import ai.harmony.ravel.compiler.symbol.VariableSymbol;
import ai.harmony.ravel.compiler.types.PrimitiveType;
import ai.harmony.ravel.compiler.types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/25/17.
 */
public abstract class BaseIRTranslator implements IRTranslator, LoopTreeVisitor, TInstructionVisitor {
    private final StringBuilder builder;
    private final Map<Integer, String> registerNames = new HashMap<>();
    private TypedIR ir;

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

    protected void addLine(Object... elements) {
        for (Object el : elements) {
            if (el instanceof Integer)
                addCode(getRegisterName(((Integer) el)));
            else
                addCode(el.toString());
        }
        addCode(";\n");
    }

    protected void declareControllerScope(VariableSymbol sym) {
        setRegisterName(sym.getRegister(), sym.getName());
    }

    protected void declareEventScope(VariableSymbol sym) {
        setRegisterName(sym.getRegister(), sym.getName());
    }

    protected abstract void declareRegister(int reg, Type type);

    protected Type getRegisterType(int reg) {
        return ir.getRegisterType(reg);
    }

    @Override
    public void translate(TypedIR ir) {
        registerNames.clear();
        builder.setLength(0);
        this.ir = ir;

        // declare all the controller parameters
        for (VariableSymbol sym : ir.getClassScopeVariables())
            declareControllerScope(sym);
        // declare all event parameters
        for (VariableSymbol sym : ir.getArguments())
            declareEventScope(sym);
        // declare the return value, if present
        if (ir.getRegisterType(Registers.RETURN_REG) != PrimitiveType.VOID) {
            setRegisterName(Registers.RETURN_REG, "__returnValue");
            declareRegister(Registers.RETURN_REG, ir.getRegisterType(Registers.RETURN_REG));
        }
        for (VariableSymbol sym : ir.getLocalVariables()) {
            Type type = ir.getRegisterType(sym.getRegister());
            if (type == PrimitiveType.ANY)
                continue;
            setRegisterName(sym.getRegister(), sym.getName());
            declareRegister(sym.getRegister(), type);
        }

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
