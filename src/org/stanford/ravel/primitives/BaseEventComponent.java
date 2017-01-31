package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.types.EventType;

import java.util.*;

/**
 * Utility base class for InstantiatedModel, InstantiatedSource and InstantiatedSink
 *
 * Created by gcampagn on 1/30/17.
 */
abstract class BaseEventComponent extends ParametrizedComponent implements EventComponent {
    private final Space mSpace;
    private final String mVarName;

    private final List<LinkedEvent> mEvents = new ArrayList<>();
    private boolean frozen = false;

    private final Map<String, Collection<InstantiatedController>> mControllerMap = new HashMap<>();
    private final List<InstantiatedController> mControllerList = new ArrayList<>();

    BaseEventComponent(Space space, Primitive primitive, String varName) {
        super(primitive.getName());
        mSpace = space;
        mVarName = varName;
    }

    void createEvent(String eventName) {
        mControllerMap.put(eventName, new HashSet<>());
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

        Set<InstantiatedController> allControllers = new HashSet<>();
        for (LinkedEvent e : mEvents) {
            allControllers.add(e.getController());

            EventType eventType = e.getHandler().getEventType();
            mControllerMap.get(eventType.getEventName()).add(e.getController());
        }

        // convert the set to a list for stable ordering
        mControllerList.addAll(allControllers);
    }

    public Map<String, Collection<InstantiatedController>> getControllerMap() {
        return Collections.unmodifiableMap(mControllerMap);
    }
    public List<InstantiatedController> getControllerList() {
        return Collections.unmodifiableList(mControllerList);
    }

    public String getVarName() {
        return mVarName;
    }
}
