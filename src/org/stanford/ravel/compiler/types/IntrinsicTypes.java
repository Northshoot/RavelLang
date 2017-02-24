package org.stanford.ravel.compiler.types;

/**
 * Magic types that should be handled by the backend and quirked almost always
 *
 * Created by gcampagn on 2/22/17.
 */
public class IntrinsicTypes {
    private static class GrowableByteArray extends ClassType {
        public GrowableByteArray() {
            super("GrowableByteArray");

            addMethod("write", new Type[]{new ArrayType(PrimitiveType.BYTE), PrimitiveType.INT32, PrimitiveType.INT32}, PrimitiveType.VOID);
            for (PrimitiveType type : PrimitiveType.values()) {
                addMethod("write_" + type.getName().toLowerCase(), new Type[]{type}, PrimitiveType.VOID);
            }
            addMethod("write_uint16", new Type[]{PrimitiveType.INT32}, PrimitiveType.VOID);
            addMethod("write_byte_array", new Type[]{new ArrayType(PrimitiveType.BYTE)}, PrimitiveType.VOID);

            addMethod("toByteArray", new Type[]{}, new ArrayType(PrimitiveType.BYTE));
            addStaticMethod("create", new Type[]{}, this);
        }
    }

    public static final ClassType GROWABLE_BYTE_ARRAY = new GrowableByteArray();
}