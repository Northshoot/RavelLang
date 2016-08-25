package ai.harmony.ravel.compiler.old;

/**
 * Created by lauril on 8/19/16.
 */
public class ControllerConfigBlock extends BlockSymbol{

    public ControllerConfigBlock(String name, Scope currentScope) {
        super(name, currentScope);

    }


    @Override
    public String toString() {
        return "Property Symbol:" + name;
    }
}
