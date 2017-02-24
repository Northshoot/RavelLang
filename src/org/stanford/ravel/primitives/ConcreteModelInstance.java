package org.stanford.ravel.primitives;

/**
 * Created by gcampagn on 2/21/17.
 */
public class ConcreteModelInstance extends EventComponentInstance<ConcreteModel> {
    public ConcreteModelInstance(ConcreteModel model, String varName) {
        super(model, model.getSpace(), varName);
    }

    void freeze() {
        this.freeze(getComponent().getBaseModel().getParameterNames());
    }
}
