package org.stanford.ravel.compiler.symbol;

/**
 * Created by lauril on 8/26/16.
 */
public class ReferenceSymbol extends BaseSymbol implements TypedSymbol, MemberSymbol {

    private String mValue;
    private int slot = -1;
    public ReferenceSymbol(String name, String val) {
        super(name);
        this.mValue = val;
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }


    public String getValue(){
        return mValue;
    }
    @Override
    public int getSlotNumber() {
        return slot;
    }
}
