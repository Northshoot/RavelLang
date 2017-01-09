package org.stanford.ravel.compiler.symbol;

/**
 * variable that lives inside an aggregate like a
 *  class or struct.
 * Created by lauril on 8/25/16.
 */
public class LocalVariableSymbol extends VariableSymbol implements MemberSymbol {
    protected int slot;

    public LocalVariableSymbol(String name) {
        super(name);
    }

    @Override
    public int getSlotNumber() { return slot; }
}