package org.stanford.ravel.primitives;

import org.stanford.ravel.analysis.Operation;
import org.stanford.ravel.analysis.TriConsumer;
import org.stanford.ravel.compiler.symbol.FieldSymbol;
import org.stanford.ravel.compiler.types.Type;

import java.util.*;

/**
 * A model field, which has a name, a type, and a set of security/operation tags
 * computed by the various analysis passes
 *
 * Created by gcampagn on 2/9/17.
 */
public class ModelField {
    private final FieldSymbol symbol;
    private final Map<Space, Map<Space, Operation>> operations = new HashMap<>();
    private final Set<Space> writers = new HashSet<>();

    public ModelField(FieldSymbol symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return symbol.getName();
    }

    public Type getType() {
        return symbol.getType();
    }

    public String toString() {
        return "Field: " + getName() + " @" + getType().getName();
    }

    public void addOperation(Space operator, Space creator, Operation op) {
        operations.computeIfAbsent(operator, (key) -> new HashMap<>()).put(creator, op);
    }
    public void forEachOperation(TriConsumer<? super Space, ? super Space, ? super Operation> consumer) {
        operations.forEach((space, spaceOperations) -> {
            spaceOperations.forEach((creator, op) -> {
                consumer.apply(space, creator, op);
            });
        });
    }
    public Map<Space, Operation> getOperations(Space operator) {
        return operations.getOrDefault(operator, Collections.emptyMap());
    }

    public void addWriter(Space space) {
        writers.add(space);
    }
    public Collection<Space> getWriters() {
        return Collections.unmodifiableCollection(writers);
    }
}