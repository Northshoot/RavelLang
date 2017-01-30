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

    InstantiatedController(Space space, Controller controller) {
        super(controller.getName(), controller.getName() + "Ctr");
        mSpace = space;
        mController = controller;
    }

    public Controller getController() {
        return mController;
    }

    public Collection<InstantiatedModel> getLinkedModels() {
        Set<InstantiatedModel> models = new HashSet<>();
        for (LinkedEvent event : mLinkedEvents)
            models.add(event.getModel());
        return models;
    }

    public void linkEvent(EventHandler event, InstantiatedModel model) {
        mLinkedEvents.add(new LinkedEvent(model, event));
    }

    public Iterator<LinkedEvent> iterator() {
        return mLinkedEvents.iterator();
    }
}
