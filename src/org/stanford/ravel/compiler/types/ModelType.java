package org.stanford.ravel.compiler.types;

import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.primitives.ModelEvent;

import java.util.Arrays;
import java.util.Collection;

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

    private final RecordType recordType;
    private final ContextType ctxType;

    public ModelType(ModelSymbol symbol) {
        super(symbol.getName());

        recordType = new RecordType(symbol.getName() + "::Record");
        ctxType = new ContextType();

        // If you add new functions here, you must update LocalOwnershipTaggingPass
        // to handle them correctly
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

    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * The type of "self" in an event handler
     *
     * Created by gcampagn on 1/29/17.
     */
    public class ContextType implements CompoundType {
        public ModelType getOwner() {
            return ModelType.this;
        }

        @Override
        public Collection<String> getMemberList() {
            return Arrays.asList("record", "error");
        }

        @Override
        public Type getMemberType(String member) {
            switch (member) {
                case "error":
                    return PrimitiveType.ERROR_MSG;
                case "record":
                    return getRecordType();
                default:
                    return null;
            }
        }

        /**
         * No member of context types is writable.
         * This is important for alias analysis.
         *
         * @param member ignored
         * @return false
         */
        @Override
        public boolean isWritable(String member) {
            return false;
        }

        @Override
        public String getName() {
            return ModelType.this.getName() + "::Context";
        }
    }
}
