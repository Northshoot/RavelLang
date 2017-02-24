package org.stanford.ravel.primitives;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by gcampagn on 2/21/17.
 */
public class EventComponentInstance<EC extends BaseEventComponent> extends ComponentInstance<EC> {
    private final List<ConcreteControllerInstance> mControllers = new ArrayList<>();

    EventComponentInstance(EC component, Space space, String varName) {
        super(component, space, varName);
    }

    public void linkController(ConcreteControllerInstance controller) {
        mControllers.add(controller);
    }

    public Collection<ConcreteControllerInstance> getControllerList() {
        return Collections.unmodifiableCollection(mControllers);
    }

    public EC getComponent() {
        return super.getComponent();
    }
}
