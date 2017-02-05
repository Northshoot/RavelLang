package org.stanford.ravel.primitives;

/**
 * Mark that the data is flowing from one controller to another
 *
 * The controllers need not be on different spaces, but they usually would.
 * Indeed, the flow primitive is used to compute the direction of transmission
 * for streaming models.
 *
 * Created by lauril on 8/16/16.
 */
public class Flow {
    private final InstantiatedController source;
    private final InstantiatedController sink;
    private final Model model;

    public Flow(InstantiatedController from, InstantiatedController to, Model model) {
        super();
        assert from != to;
        this.source = from;
        this.sink = to;
        this.model = model;
    }

    public InstantiatedController getSource() {
        return source;
    }

    public InstantiatedController getSink() {
        return sink;
    }

    public Model getModel() {
        return model;
    }

    public String toString() {
        return model.getName() + ": from " + source.getSpace().getName() + "." + source.getVarName() + " to "
                + sink.getSpace().getName() + "." + sink.getVarName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flow flow = (Flow) o;

        if (!source.equals(flow.source)) return false;
        if (!sink.equals(flow.sink)) return false;
        return model.equals(flow.model);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + sink.hashCode();
        result = 31 * result + model.hashCode();
        return result;
    }
}
