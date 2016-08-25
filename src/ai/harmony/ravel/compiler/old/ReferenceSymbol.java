package ai.harmony.ravel.compiler.old;

/**
 * Created by lauril on 8/19/16.
 */
public class ReferenceSymbol extends Symbol  {
    String mInstanceName;

    public ReferenceSymbol( String name,  String instanceName ) {
        super(name, Type.INSTANTIATION);
        this.mInstanceName = instanceName;
    }
}
