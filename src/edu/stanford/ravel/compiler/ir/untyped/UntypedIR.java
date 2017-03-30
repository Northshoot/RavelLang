package edu.stanford.ravel.compiler.ir.untyped;

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
