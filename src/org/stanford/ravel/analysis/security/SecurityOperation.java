package org.stanford.ravel.analysis.security;

import org.stanford.ravel.primitives.ModelField;
import org.stanford.ravel.primitives.Space;

import java.util.HashSet;
import java.util.Set;

/**
 * A security operation consists a security primitive, a key, a set of model
 * fields to which it applies, and a corresponding endpoint.
 *
 * It reduces to IR which is then compiled to the target platform.
 */
public class SecurityOperation {
    private final Key key;
    private final Set<ModelField> fields = new HashSet<>();
    private final SecurityPrimitive primitive;
    private final Space target;

    public SecurityOperation(Key key, SecurityPrimitive primitive, Space target) {
        this.key = key;
        this.primitive = primitive;
        this.target = target;
    }

    @Override
    public String toString() {
        return primitive + " fields " + fields + " with key " + key + " when talking to " + target.getName();
    }

    public Key getKey() {
        return key;
    }

    public SecurityPrimitive getPrimitive() {
        return primitive;
    }

    public Space getTarget() {
        return target;
    }

    public void addField(ModelField field) {
        fields.add(field);
    }

    public boolean hasAnyField() {
        return !fields.isEmpty();
    }
}
