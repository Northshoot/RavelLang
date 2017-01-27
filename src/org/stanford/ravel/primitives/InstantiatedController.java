package org.stanford.ravel.primitives;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public void linkEvent(Event event, Model model) {
        mLinkedEvents.add(new LinkedEvent(model, event));
    }

    public Iterator<LinkedEvent> iterator() {
        return mLinkedEvents.iterator();
    }
}
