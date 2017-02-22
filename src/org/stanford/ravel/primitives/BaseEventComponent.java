package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.types.EventType;

import java.util.*;

/**
 * Utility base class for ConcreteModel, InstantiatedSource and InstantiatedSink
 *
 * Created by gcampagn on 1/30/17.
 */
abstract class BaseEventComponent extends Primitive implements EventComponent {
    private final Space mSpace;

    private final List<LinkedEvent> mEvents = new ArrayList<>();
    private boolean frozen = false;

    private final Map<String, Map<Integer, Collection<LinkedEvent>>> mControllerMap = new HashMap<>();
    private final List<ConcreteControllerInstance> mControllerList = new ArrayList<>();

    BaseEventComponent(Space space, Primitive primitive) {
        super(primitive.getName());
        mSpace = space;
    }

    @Override
    public String toString() {
        return "Event Component " + getName();
    }

    Space getSpace() {
        return mSpace;
    }

    void createEvent(String eventName) {
        mControllerMap.put(eventName, new HashMap<>());
    }

    @Override
    public void addLinkedEvent(LinkedEvent event) {
        assert !frozen;
        mEvents.add(event);
    }

    /**
     * Build derived state after all primary state is ready
     */
    void freeze() {
        assert !frozen;
        frozen = true;

        Set<ConcreteControllerInstance> allControllers = new HashSet<>();
        for (LinkedEvent e : mEvents) {
            allControllers.add(e.getControllerInstance());

            EventType eventType = e.getHandler().getEventType();
            mControllerMap.get(eventType.getEventName())
                .computeIfAbsent(e.getComponentInstance().getInstanceId(), (key) -> new HashSet<>()).add(e);
        }

        // convert the set to a list for stable ordering
        mControllerList.addAll(allControllers);
    }

    public Map<String, Map<Integer, Collection<LinkedEvent>>> getControllerMap() {
        assert frozen;
        return Collections.unmodifiableMap(mControllerMap);
    }
    public List<ConcreteControllerInstance> getControllerList() {
        assert frozen;
        return Collections.unmodifiableList(mControllerList);
    }
}
