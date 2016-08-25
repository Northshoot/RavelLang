package ai.harmony.ravel.compiler.old;

/**
 * Created by lauril on 8/18/16.
 */
public class FlowScope  extends BaseScope {
        public FlowScope(Scope parent) { super(parent); }
    public String getScopeName() { return "flow"; }
}
