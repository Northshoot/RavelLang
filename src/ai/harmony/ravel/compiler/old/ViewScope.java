package ai.harmony.ravel.compiler.old;

/**
 * Created by lauril on 8/18/16.
 */
public class ViewScope extends BaseScope {
    public ViewScope(Scope parent) { super(parent); }
    public String getScopeName() { return "view"; }
}
