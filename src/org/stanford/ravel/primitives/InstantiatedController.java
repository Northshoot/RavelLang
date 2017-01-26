package org.stanford.ravel.primitives;

/**
 * A primitive representing the use of a Controller in a Space
 *
 * Created by gcampagn on 1/26/17.
 */
public class InstantiatedController extends ParametrizedComponent {
    private final Space mSpace;
    private final Controller mController;

    public InstantiatedController(Space space, Controller controller) {
        super(controller.getName(), controller.getName() + "Ctr");
        mSpace = space;
        mController = controller;
    }
}
