package edu.stanford.ravel.primitives;

/**
 * Created by gcampagn on 3/22/17.
 */
public class ConcreteViewInstance extends EventComponentInstance<ConcreteView> {
    public ConcreteViewInstance(ConcreteView view, String varName) {
        super(view, view.getSpace(), varName);
    }

    void freeze() {
        this.freeze(getComponent().getBaseView().getParameterNames());
    }
}
