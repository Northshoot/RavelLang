package org.stanford.ravel.primitives;

import java.util.*;

/**
 * Created by gcampagn on 1/26/17.
 */
public class InstantiatedModel extends ParametrizedComponent {
    private final Space mSpace;
    private final Model mModel;
    private final String mVarName;

    private final List<LinkedEvent> mEvents = new ArrayList<>();
    private boolean frozen = false;

    private final Map<String, Collection<InstantiatedController>> mControllerMap = new HashMap<>();
    private final List<InstantiatedController> mControllerList = new ArrayList<>();

    InstantiatedModel(Space space, Model model, String varName) {
        super(model.getName(), model.getName() + "Model");
        mSpace = space;
        mModel = model;
        mVarName = varName;

        for (ModelEvent e : ModelEvent.values())
            mControllerMap.put(e.name(), new HashSet<>());
    }

    void addLinkedEvent(LinkedEvent event) {
        assert !frozen;
        mEvents.add(event);
    }

    /**
     * Build derived state after all primary state is ready
     */
    public void freeze() {
        assert !frozen;
        frozen = true;

        Set<InstantiatedController> allControllers = new HashSet<>();
        for (LinkedEvent e : mEvents) {
            allControllers.add(e.getController());
            mControllerMap.get(e.getHandler().getEvent().name()).add(e.getController());
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

    public int getSize() {
        return (Integer) getParam("size");
    }

    public boolean isReliable() {
        return (Boolean) getParam("reliable");
    }
    public boolean isDurable() {
        return (Boolean) getParam("durable");
    }

    public Model getBaseModel() {
        return mModel;
    }
}
