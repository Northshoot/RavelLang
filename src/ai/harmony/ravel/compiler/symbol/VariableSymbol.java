package ai.harmony.ravel.compiler.symbol;

public class VariableSymbol<T> extends BaseSymbol implements MemberSymbol{

    private T value;
    private boolean mArray = false;

    public VariableSymbol(String name) {
        super(name);
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }

    public void setValue(T value){
        this.value = value;
    }

    public T getValue() {return this.value ;}

    public boolean isArray(){return mArray; }
    public void setArray(){mArray = true ; }
    @Override
    public int getSlotNumber() {
        return 0;
    }
}