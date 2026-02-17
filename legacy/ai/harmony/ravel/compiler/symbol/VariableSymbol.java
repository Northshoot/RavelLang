package ai.harmony.ravel.compiler.symbol;

import ai.harmony.ravel.compiler.ir.Registers;
import ai.harmony.ravel.compiler.types.Type;

public class VariableSymbol extends BaseSymbol implements TypedSymbol {
    private int register = Registers.UNSET_REG;
    private boolean writable = true;

    public VariableSymbol(String name) {
        super(name);
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }

    public void setRegister(int reg) {
        register = reg;
    }
    public int getRegister() {
        return register;
    }

    public boolean isWritable() {
        return writable;
    }
    public void setWritable(boolean writable) {
        this.writable = writable;
    }
}