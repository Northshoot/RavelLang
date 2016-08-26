package ai.harmony.ravel.compiler.symbol;

/**
 * Created by lauril on 8/25/16.
 */
public class EventSymbol extends SymbolWithScope implements MemberSymbol{
    protected int slot = -1;
    public EventSymbol(String name) {
        super(name);
    }

    @Override
    public int getSlotNumber() {
        return slot;
    }
}
