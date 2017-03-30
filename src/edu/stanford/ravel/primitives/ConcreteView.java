package edu.stanford.ravel.primitives;

import edu.stanford.ravel.compiler.symbol.MethodDeclarationSymbol;

/**
 * Created by gcampagn on 3/22/17.
 */
public class ConcreteView extends BaseEventComponent<View> {
    ConcreteView(Space space, View view) {
        super(space, view);
        for (MethodDeclarationSymbol event : view.getEvents())
            createEvent(event.getName());
    }

    public View getBaseView() {
        return getBasePrimitive();
    }

    @Override
    public String toString() {
        return "Concrete View " + getSpace().getName() + "." + getName();
    }
}
