package edu.stanford.ravel.compiler.types;

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
            addStaticMethod("create", new Type[]{}, getInstanceType());
        }
    }
//TODO: extend endpoint type
    private static class Endpoint extends ClassType {
        public Endpoint() {
            super("Endpoint");
            //Add methods to the endpoint
            addMethod("getId", new Type[]{}, PrimitiveType.INT32);
            addMethod("isLocal", new Type[]{}, PrimitiveType.BOOL);
        }
    }

    public static final ClassType GROWABLE_BYTE_ARRAY = new GrowableByteArray();
    public static final ClassType ENDPOINT = new Endpoint();
    public static final Type KEY = new Type() {
        @Override
        public String getName() {
            return "Key";
        }
    };
}
