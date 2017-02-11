package org.stanford.ravel.primitives;

import java.util.Iterator;
import java.util.List;

/**
 * Mark that the data is flowing from one space to another
 *
 * A flow is always directed (unlike a FlowSymbol). For replicated models,
 * which use undirected flows, we generate as many flows as needed.
 *
 * Created by lauril on 8/16/16.
 */
public class Flow implements Iterable<Space> {
    private final List<Space> spaces;
    private final Model model;

    public Flow(List<Space> spaces, Model model) {
        super();
        assert spaces.size() >= 2;
        this.spaces = spaces;
        this.model = model;
    }

    public Iterator<Space> iterator() {
        return spaces.iterator();
    }

    public Space getSource() {
        return spaces.get(0);
    }

    public Space getSink() {
        return spaces.get(spaces.size()-1);
    }

    public Model getModel() {
        return model;
    }

    public boolean involvesSpace(Space s) {
        return spaces.contains(s);
    }

    public Space getNext(Space s) {
        int i = spaces.indexOf(s);
        if (i < 0 || i == spaces.size()-1)
            return null;
        return spaces.get(i+1);
    }

    public Space getPrevious(Space s) {
        int i = spaces.indexOf(s);
        if (i <= 0)
            return null;
        return spaces.get(i-1);
    }

    public String toString() {
        return model.getName() + ": from " + getSource().getName() + " to "
                + getSink().getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flow flow = (Flow) o;

        if (!spaces.equals(flow.spaces)) return false;
        return model.equals(flow.model);
    }

    @Override
    public int hashCode() {
        int result = spaces.hashCode();
        result = 31 * result + model.hashCode();
        return result;
    }
}
