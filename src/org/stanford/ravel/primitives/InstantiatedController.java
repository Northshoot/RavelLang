package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.VariableSymbol;

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
        Set<InstantiatedModel> models = new HashSet<>();
        for (LinkedEvent event : mLinkedEvents)
            models.add(event.getModel());
        return models;
    }

    public void linkEvent(EventHandler event, InstantiatedModel model) {
        LinkedEvent linkedEvent = new LinkedEvent(this, model, event);
        mLinkedEvents.add(linkedEvent);
        model.addLinkedEvent(linkedEvent);
    }

    public Iterator<LinkedEvent> iterator() {
        return mLinkedEvents.iterator();
    }
}
