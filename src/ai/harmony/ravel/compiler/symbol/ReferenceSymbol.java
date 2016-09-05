package ai.harmony.ravel.compiler.symbol;

/**
 * Created by lauril on 8/26/16.
 */
public class ReferenceSymbol extends BaseSymbol implements TypedSymbol, MemberSymbol {
    private String mReference;
    private int slot = -1;
    public ReferenceSymbol(String name, String ref) {
        super(name);
        this.mReference = ref;
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }

    public String getReference(){
        return mReference;
    }
    @Override
    public int getSlotNumber() {
        return slot;
    }
}
