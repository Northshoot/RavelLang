package ai.harmony.ravel.compiler.symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the assignment to the flow property
 * in a streaming or replicated model
 *
 * Created by gcampagn on 2/10/17.
 */
public class FlowSymbol extends BaseSymbol {
    private final List<String> spaces = new ArrayList<>();
    private final boolean directed;

    public FlowSymbol(boolean directed) {
        super("flow");
        this.directed = directed;
    }

    public void addSpace(String space) {
        spaces.add(space);
    }

    public List<String> getSpaces() {
        return Collections.unmodifiableList(spaces);
    }

    public boolean isDirected() {
        return directed;
    }
}
