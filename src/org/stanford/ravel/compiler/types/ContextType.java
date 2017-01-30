package org.stanford.ravel.compiler.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * The type of "self" in an event handler
 *
 * Created by gcampagn on 1/29/17.
 */
class ContextType implements CompoundType {
    private final ClassType component;

    ContextType(ClassType model) {
        this.component = model;
    }

    @Override
    public Collection<String> getMemberList() {
        if (component instanceof ModelType)
            return Arrays.asList("record", "error");
            return Collections.singletonList("error");
    }

    @Override
    public Type getMemberType(String member) {
        switch (member) {
            case "error":
                return PrimitiveType.ERROR_MSG;
            case "record":
                if (component instanceof ModelType)
                    return ((ModelType) component).getRecordType();
                else
                    return null;
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
        return this.component.getName() + "::Context";
    }
}
