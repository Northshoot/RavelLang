package org.stanford.ravel.primitives;

/**
 * Created by gcampagn on 1/26/17.
 */
public class InstantiatedModel extends ParametrizedComponent {
    private final Space mSpace;
    private final Model mModel;

    InstantiatedModel(Space space, Model model) {
        super(model.getName(), model.getName() + "Model");
        mSpace = space;
        mModel = model;
    }

    public int getSize() {
        return (Integer) getParam("size");
    }
}
