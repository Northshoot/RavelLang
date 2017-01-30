package org.stanford.ravel.primitives;

import java.util.*;

/**
 * A primitive representing the use of a Controller in a Space
 *
 * Created by gcampagn on 1/26/17.
 */
public class InstantiatedController extends ParametrizedComponent implements Iterable<LinkedEvent> {
    private final Space mSpace;
    private final Controller mController;
    private final List<LinkedEvent> mLinkedEvents = new ArrayList<>();
    private final String mVarName;

    InstantiatedController(Space space, Controller controller, String varName) {
        super(controller.getName(), controller.getName() + "Ctr");
        mSpace = space;
        mController = controller;
        mVarName = varName;
    }

    public Controller getController() {
        return mController;
    }

    public String getVarName() {
        return mVarName;
    }

    public Collection<InstantiatedModel> getLinkedModels() {
        return getLinkedComponents(InstantiatedModel.class);
    }
    public Collection<InstantiatedInterface> getLinkedInterfaces() {
        return getLinkedComponents(InstantiatedInterface.class);
    }

    private <E extends EventComponent> Collection<E> getLinkedComponents(Class<E> ofClass) {
        Set<E> components = new HashSet<E>();
        for (LinkedEvent event : mLinkedEvents) {
            if (ofClass.isInstance(event.getComponent())) {
                components.add(ofClass.cast(event.getComponent()));
            }
        }
        return components;
    }

    public void linkEvent(EventHandler event, EventComponent component) {
        LinkedEvent linkedEvent = new LinkedEvent(this, component, event);
        mLinkedEvents.add(linkedEvent);
        component.addLinkedEvent(linkedEvent);
    }

    public Iterator<LinkedEvent> iterator() {
        return mLinkedEvents.iterator();
    }
}
