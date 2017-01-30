package org.stanford.ravel.compiler.types;

import org.stanford.ravel.compiler.symbol.ModelSymbol;

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

    private ModelSymbol symbol;
    private final StructType recordType;
    private final ContextType ctxType;

    public ModelType(ModelSymbol symbol) {
        super(symbol.getName());

        recordType = new RecordType(symbol.getName() + "::Record");

        this.addStaticMethod("create", new Type[0], recordType);
        this.addMethod("save", new Type[]{recordType}, PrimitiveType.VOID);

        ctxType = new ContextType(this);
    }

    public ModelSymbol getSymbol() {
        return symbol;
    }

    public StructType getRecordType() {
        return recordType;
    }

    public CompoundType getContextType() { return ctxType; }
}
