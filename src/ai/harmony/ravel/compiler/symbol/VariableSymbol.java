package ai.harmony.ravel.compiler.symbol;

public class VariableSymbol extends BaseSymbol implements TypedSymbol {
    public VariableSymbol(String name) {
        super(name);
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }
}