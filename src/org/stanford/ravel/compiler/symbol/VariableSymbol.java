package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.ir.untyped.UntypedIR;
import org.stanford.ravel.compiler.types.Type;

public class VariableSymbol extends BaseSymbol implements TypedSymbol {
    private int register = UntypedIR.UNSET_REG;

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
}