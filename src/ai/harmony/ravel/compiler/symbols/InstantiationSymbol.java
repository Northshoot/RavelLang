package ai.harmony.ravel.compiler.symbols;

/**
 * Created by lauril on 8/19/16.
 */
public class InstantiationSymbol extends Symbol  {
    String mRefered;

    public InstantiationSymbol( String name,  String refered ) {
        super(name, Type.REFERENCE);
        this.mRefered= refered;
    }
}
