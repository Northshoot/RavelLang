package ai.harmony.ravel.compiler.symbol;

public class VariableSymbol extends BaseSymbol implements TypedSymbol, MemberSymbol{
    public VariableSymbol(String name) {
        super(name);
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }

    @Override
    public int getSlotNumber() {
        return 0;
    }
}