package ai.harmony.ravel.compiler.symbol;

public class VariableSymbol extends BaseSymbol implements MemberSymbol{

    private String value;

    public VariableSymbol(String name) {
        super(name);
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getValue() {return this.value ;};
    @Override
    public int getSlotNumber() {
        return 0;
    }
}