package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.VariableSymbol;

import java.util.*;

/**
 * A primitive representing the use of a Controller in a Space
 *
 * Created by gcampagn on 1/26/17.
 */
public class ConcreteController extends Primitive implements Iterable<LinkedEvent> {
    private final Space mSpace;
    private final Controller mController;
    private final List<LinkedEvent> mLinkedEvents = new ArrayList<>();
    private final Set<EventComponent> mEventComponents = new HashSet<>();

    ConcreteController(Space space, Controller controller) {
        super(controller.getName());
        mSpace = space;
        mController = controller;
    }

    @Override
    public String toString() {
        return "Concrete Controller " + mSpace.getName() + "." + getName();
    }

    public Space getSpace() {
        return mSpace;
    }

    public Controller getController() {
        return mController;
    }

    public Collection<ConcreteModel> getLinkedModels() {
        return getLinkedComponents(ConcreteModel.class);
    }
    public Collection<ConcreteInterface> getLinkedInterfaces() {
        return getLinkedComponents(ConcreteInterface.class);
    }
    public Collection<ConcreteView> getLinkedViews() {
        return getLinkedComponents(ConcreteView.class);
    }

    private <E extends EventComponent> Collection<E> getLinkedComponents(Class<E> ofClass) {
        Set<E> components = new HashSet<E>();
        for (EventComponent object : mEventComponents) {
            if (ofClass.isInstance(object)) {
                components.add(ofClass.cast(object));
            }
        }
        return components;
    }

    public void addLinkedEvent(LinkedEvent event) {
        mLinkedEvents.add(event);
    }
    public void linkComponent(EventComponent component) {
        mEventComponents.add(component);
    }

    public Iterator<LinkedEvent> iterator() {
        return mLinkedEvents.iterator();
    }
}
