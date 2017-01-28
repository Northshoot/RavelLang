package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.types.Type;

/**
 * A symbol representing a reference to some external object (a source or a sink, defined
 * by the platform)
 *
 * Created by lauril on 8/26/16.
 */
public class ReferenceSymbol extends BaseSymbol implements MemberSymbol {

    private String mValue;
    private int slot = -1;
    public ReferenceSymbol(String name, String val) {
        super(name);
        this.mValue = val;
    }

    public String getValue(){
        return mValue;
    }
    @Override
    public int getSlotNumber() {
        return slot;
    }
}
