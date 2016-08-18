package ai.harmony.ravel.compiler.scopes;

/**
 * Created by lauril on 8/18/16.
 */
public class ControllerScope  extends BaseScope{
    public ControllerScope(Scope parent) { super(parent); }
    public String getScopeName() { return "controller"; }
}
