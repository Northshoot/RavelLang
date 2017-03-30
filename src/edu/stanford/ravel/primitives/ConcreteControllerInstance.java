package edu.stanford.ravel.primitives;

/**
 * Created by gcampagn on 2/21/17.
 */
public class ConcreteControllerInstance extends ComponentInstance<ConcreteController> {
    public ConcreteControllerInstance(ConcreteController controller, String varName) {
        super(controller, controller.getSpace(), varName);
    }

    void freeze() {
        this.freeze(getComponent().getController().getParameterNames());
    }

    public void linkEvent(EventHandler event, EventComponentInstance instance) {
        LinkedEvent linkedEvent = new LinkedEvent(this, instance, event);
        getComponent().addLinkedEvent(linkedEvent);
        instance.getComponent().addLinkedEvent(linkedEvent);
        instance.linkController(this);
    }
}
