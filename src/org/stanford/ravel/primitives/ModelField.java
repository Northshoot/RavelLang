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
    public static class FieldOffset {
        private final int base;
        private final List<ModelField> addends = new ArrayList<>();

        public FieldOffset(int base) {
            this.base = base;
        }
        public FieldOffset(int base, Collection<ModelField> previous) {
            this.base = base;
            addends.addAll(previous);
        }

        @Override
        public String toString() {
            return base + " + " + addends;
        }

        public boolean isConstant() {
            return addends.isEmpty();
        }
        public int getOffset() {
            if (isConstant())
                return base;
            else
                return -1;
        }
        public int getBase() {
            return base;
        }
        public List<ModelField> getAddends() {
            return Collections.unmodifiableList(addends);
        }
    }

    private final FieldSymbol symbol;
    private final Model model;
    private final Map<Space, Map<Space, Operation>> operations = new HashMap<>();
    private FieldOffset offset;

    public ModelField(FieldSymbol symbol, Model model) {
        this.symbol = symbol;
        this.model = model;
    }

    public String getName() {
        return symbol.getName();
    }

    public Type getType() {
        return symbol.getType();
    }

    public Model getModel() {
        return model;
    }

    public String toString() {
        return "Field: " + getModel().getName() + "." + getName() + " @" + getType().getName();
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

    void setOffset(FieldOffset offset) {
        this.offset = offset;
    }

    public FieldOffset getOffset() {
        return offset;
    }
}
