package ai.harmony.ravel.primitives;

/**
 * An instance of a concrete interface, with some set of parameters
 *
 * Created by gcampagn on 2/21/17.
 */
public class ConcreteInterfaceInstance extends EventComponentInstance<ConcreteInterface> {
    public ConcreteInterfaceInstance(ConcreteInterface iface, String varName) {
        super(iface, iface.getSpace(), varName);
    }

    void freeze() {
        this.freeze(getComponent().getBaseInterface().getParameterNames());
    }
}
