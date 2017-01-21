package org.stanford.ravel.compiler.ir.untyped;

import org.stanford.ravel.compiler.ir.untyped.Block;
import org.stanford.ravel.compiler.ir.untyped.InstructionVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/19/17.
 */
public class UntypedIR {
    public final static int UNSET_REG = 0;
    public final static int VOID_REG = -1;
    public final static int ERROR_REG = -2;

    private Block rootBlock = new Block();
    private int nextRegister = 1;
    private Map<Integer, String> registerNames = new HashMap<>();

    public UntypedIR() {}

    public void accept(InstructionVisitor visitor) {
        rootBlock.accept(visitor);
    }

    public Block getRoot() {
        return rootBlock;
    }

    public int allocateRegister() {
        return nextRegister++;
    }

    // for pretty printing of code
    public void setRegisterName(int reg, String name) {
        registerNames.put(reg, name);
    }
    public String getRegisterName(int reg) {
        String name = registerNames.get(reg);
        if (name == null)
            return Integer.toString(reg);
        else
            return name;
    }
}
