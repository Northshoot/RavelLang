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
    private ModelSymbol symbol;
    private final StructType recordType;

    public ModelType(ModelSymbol symbol) {
        super(symbol.getName());

        recordType = new StructType(symbol.getName() + "::Record");
        this.addField("record", recordType);
        this.addField("error", PrimitiveType.ERROR_MSG);

        this.addStaticMethod("create", new Type[0], recordType);
        this.addStaticMethod("save", new Type[]{recordType}, PrimitiveType.VOID);
    }

    public ModelSymbol getSymbol() {
        return symbol;
    }

    public StructType getRecordType() {
        return recordType;
    }
}
