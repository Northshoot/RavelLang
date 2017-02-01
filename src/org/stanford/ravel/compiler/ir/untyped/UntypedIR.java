package org.stanford.ravel.compiler.ir.untyped;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.ir.untyped.Block;
import org.stanford.ravel.compiler.ir.untyped.InstructionVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/19/17.
 */
public class UntypedIR {

    private Block rootBlock = new Block();
    private int nextRegister;

    public UntypedIR(int firstGpRegister) {
        nextRegister = firstGpRegister;
    }

    public Block getRoot() {
        return rootBlock;
    }

    public int allocateRegister() {
        return nextRegister++;
    }

    public int numUsedRegisters() {
        return nextRegister;
    }
}
