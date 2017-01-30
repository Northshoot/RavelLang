package org.stanford.ravel.compiler.types;

import java.util.Arrays;
import java.util.Collection;

/**
 * The type of "self" in an event handler
 *
 * Created by gcampagn on 1/29/17.
 */
public class ContextType implements CompoundType {
    private final ModelType model;

    public ContextType(ModelType model) {
        this.model = model;
    }

    @Override
    public Collection<String> getMemberList() {
        return Arrays.asList("record", "error", "model");
    }

    @Override
    public Type getMemberType(String member) {
        switch (member) {
            case "record":
                return model.getRecordType();
            case "error":
                return PrimitiveType.ERROR_MSG;
            case "model":
                return model.getInstanceType();
            default:
                return null;
        }
    }

    @Override
    public boolean isWritable(String member) {
        return false;
    }

    @Override
    public String getName() {
        return this.model.getName() + "::Context";
    }
}
