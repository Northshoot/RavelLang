package edu.stanford.ravel.compiler.types;

import edu.stanford.ravel.compiler.symbol.ModelSymbol;
import edu.stanford.ravel.primitives.Model;
import edu.stanford.ravel.primitives.ModelEvent;

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
        private RecordType(String name, boolean mutable) {
            super(name, mutable);
        }

        public ModelType getModel() {
            return ModelType.this;
        }

        @Override
        StructType constructImmutable() {
            return new RecordType(getName(), false);
        }
    }

    private final RecordType recordType;
    private final RecordType immutableRecordType;
    private final ContextType ctxType;
    private final IteratorType iteratorType;
    private final Model.Type modelType;

    public ModelType(ModelSymbol symbol, Model.Type modelType) {
        super(symbol.getName());

        recordType = new RecordType(symbol.getName() + ".Record");
        immutableRecordType = (RecordType) recordType.makeImmutable();
        ctxType = new ContextType();
        this.modelType = modelType;

        Type queryRecordType;
        switch (modelType) {
            case LOCAL:
            case REPLICATED:
                queryRecordType = recordType;
                break;

            case STREAMING:
                queryRecordType = immutableRecordType;
                break;

            default:
                throw new AssertionError();
        }
        iteratorType = new IteratorType(symbol.getName() + ".Iterator", queryRecordType);

        // If you add new functions here, you must update LocalOwnershipTaggingPass
        // to handle them correctly
        if (modelType == Model.Type.REPLICATED)
            this.addMethod("save", new Type[]{immutableRecordType, IntrinsicTypes.ENDPOINT.getInstanceType()}, ctxType);
        else
            this.addMethod("save", new Type[]{immutableRecordType}, ctxType);
        this.addMethod("delete", new Type[]{immutableRecordType}, PrimitiveType.VOID);
        this.addMethod("create", new Type[0], recordType);
        this.addMethod("first", new Type[0], queryRecordType);
        this.addMethod("last", new Type[0], queryRecordType);
        this.addMethod("get", new Type[]{PrimitiveType.INT32}, queryRecordType);
        //this.addMethod("all", new Type[0], new ArrayType(queryRecordType).makeImmutable());
        this.addMethod("clear", new Type[0], PrimitiveType.VOID);
        this.addMethod("size", new Type[0], PrimitiveType.INT32);
        this.addMethod("iterator", new Type[0], iteratorType.getInstanceType());

        for (ModelEvent e : ModelEvent.values()) {
            this.addEvent(e.name(), new Type[]{ctxType}, true);
        }
    }

    public RecordType getRecordType() {
        return recordType;
    }

    @Override
    public Type getNestedType(String name) {
        switch (name) {
            case "Record":
                return recordType;
            case "Context":
                return ctxType;
            default:
                return null;
        }
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

        private ContextType() {}

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
                    return modelType == Model.Type.STREAMING ? immutableRecordType : recordType;
                case "endpoint":
                    return IntrinsicTypes.ENDPOINT.getInstanceType();
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
            return ModelType.this.getName() + ".Context";
        }
    }

    public class IteratorType extends ClassType {
        private IteratorType(String name, Type queryRecordType) {
            super(name);

            this.addMethod("hasNext", new Type[]{}, PrimitiveType.BOOL);
            this.addMethod("next", new Type[]{}, queryRecordType);
        }

        public ModelType getOwner() {
            return ModelType.this;
        }
    }
}
