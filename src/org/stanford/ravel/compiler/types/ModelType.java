package org.stanford.ravel.compiler.types;

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
    private final StructType recordType;

    public ModelType(String name) {
        super(name);

        recordType = new StructType(name + "::Record");
        this.addField("record", recordType);
        this.addField("error", PrimitiveType.ERROR_MSG);

        this.addStaticMethod("create", new Type[0], recordType);
        this.addStaticMethod("save", new Type[]{recordType}, PrimitiveType.VOID);
    }

    public StructType getRecordType() {
        return recordType;
    }
}
