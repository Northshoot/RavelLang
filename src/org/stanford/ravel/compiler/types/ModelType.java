package org.stanford.ravel.compiler.types;

import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.primitives.ModelEvent;
import org.stanford.ravel.primitives.Primitive;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * The type of a model object.
 *
 * Conceptually, it's a class type with fields
 * "record" (of record type), "error" (of error message type),
 * static method
 *
 * Created by gcampagn on 1/23/17.
 */
public class ModelType extends ClassType {
    public class RecordType extends StructType {
        private RecordType(String name) {
            super(name);
        }

        public ModelType getModel() {
            return ModelType.this;
        }
    }

    private final StructType recordType;
    private final ContextType ctxType;

    public ModelType(ModelSymbol symbol) {
        super(symbol.getName());

        recordType = new RecordType(symbol.getName() + "::Record");
        ctxType = new ContextType(this);

        this.addMethod("save", new Type[]{recordType}, ctxType);
        this.addMethod("create", new Type[0], recordType);
        this.addMethod("first", new Type[0], recordType);
        this.addMethod("last", new Type[0], recordType);
        this.addMethod("get", new Type[]{PrimitiveType.INT32}, recordType);
        this.addMethod("all", new Type[0], new ArrayType(recordType));
        this.addMethod("clear", new Type[0], PrimitiveType.VOID);

        for (ModelEvent e : ModelEvent.values()) {
            this.addEvent(e.name(), new Type[]{ctxType}, true);
        }
    }

    public StructType getRecordType() {
        return recordType;
    }

    /**
     * The type of "self" in an event handler
     *
     * Created by gcampagn on 1/29/17.
     */
    public static class ContextType implements CompoundType {
        private final ClassType component;

        ContextType(ClassType model) {
            this.component = model;
        }

        public ClassType getOwner() {
            return component;
        }

        @Override
        public Collection<String> getMemberList() {
            if (component instanceof ModelType)
                return Arrays.asList("record", "error");
            else
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
}
