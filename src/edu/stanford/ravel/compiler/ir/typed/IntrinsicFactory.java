package edu.stanford.ravel.compiler.ir.typed;

import edu.stanford.ravel.compiler.ir.Registers;
import edu.stanford.ravel.compiler.types.*;

/**
 * Created by gcampagn on 2/28/17.
 */
public class IntrinsicFactory {
    private static final ArrayType byteArray = new ArrayType(PrimitiveType.BYTE);

    private IntrinsicFactory() {}

    public static TIntrinsic createParameterSet(Type type, int param, int value) {
        return new TIntrinsic(PrimitiveType.VOID, new Type[]{ type, type }, Registers.VOID_REG, "param_set", new int[]{ param, value }, true, false, false);
    }

    public static TIntrinsic createReadRecordId(ModelType.RecordType recordType, int target, int record) {
        return new TIntrinsic(PrimitiveType.INT32, new Type[]{ recordType }, target, "read_record_id", new int[]{ record }, false, false, false);
    }

    public static TIntrinsic createWritePrimitive(PrimitiveType prim, int buffer, int offset, int value) {
        return new TIntrinsic(PrimitiveType.VOID, new Type[]{ byteArray, PrimitiveType.INT32, prim }, Registers.VOID_REG, "write_" + prim.getName().toLowerCase(), new int[]{ buffer, offset, value }, true, false, false);
    }

    public static TIntrinsic createWriteUInt16(int buffer, int offset, int value) {
        return new TIntrinsic(PrimitiveType.VOID, new Type[]{ byteArray, PrimitiveType.INT32, PrimitiveType.INT32 }, Registers.VOID_REG, "write_uint16", new int[]{ buffer, offset, value }, true, false, false);
    }

    public static TIntrinsic createExtractPrimitive(PrimitiveType prim, int target, int buffer, int offset) {
        return new TIntrinsic(prim, new Type[]{byteArray, PrimitiveType.INT32}, target, "extract_" + prim.getName().toLowerCase(), new int[]{ buffer, offset }, false, true, false);
    }

    public static TIntrinsic createExtractUInt16(int target, int buffer, int offset) {
        return new TIntrinsic(PrimitiveType.INT32, new Type[]{byteArray, PrimitiveType.INT32}, target, "extract_uint16", new int[]{ buffer, offset }, false, true, false);
    }

    public static TIntrinsic createExtractString(int target, int buffer, int offset, int size) {
        return new TIntrinsic(PrimitiveType.STR, new Type[]{new ArrayType(PrimitiveType.BYTE), PrimitiveType.INT32, PrimitiveType.INT32}, target, "extract_str", new int[]{ buffer, offset, size }, false, true, false);
    }

    public static TIntrinsic createStringLength(int target, int string) {
        return new TIntrinsic(PrimitiveType.INT32, new Type[]{PrimitiveType.STR}, target, "strlen", new int[]{string}, false, false, false);
    }

    public static TIntrinsic createArrayNew(ArrayType arrayType, int target, int length) {
        return new TIntrinsic(arrayType, new Type[]{PrimitiveType.INT32}, target, "array_new", new int[]{length}, false, false, false);
    }

    public static TIntrinsic createArrayFillRandom(int array, int offset, int length) {
        return new TIntrinsic(PrimitiveType.VOID, new Type[]{byteArray, PrimitiveType.INT32, PrimitiveType.INT32}, Registers.VOID_REG, "array_fill_random", new int[]{ array, offset, length }, true, false, false);
    }

    public static TIntrinsic createArrayLength(ArrayType arrayType, int target, int array) {
        return new TIntrinsic(PrimitiveType.INT32, new Type[]{arrayType}, target, "array_length", new int[]{array}, false, false, false);
    }

    public static TInstruction createArrayCopy(ArrayType arrayType, int target, int source, int tgtOffset, int srcOffset, int srcLength) {
        return new TIntrinsic(PrimitiveType.VOID, new Type[]{arrayType, arrayType, PrimitiveType.INT32, PrimitiveType.INT32, PrimitiveType.INT32}, Registers.VOID_REG, "array_copy", new int[]{target, source, tgtOffset, srcOffset, srcLength}, true, true, false);
    }

    public static TIntrinsic createLoadKey(int target, int system, int keyId) {
        return new TIntrinsic(IntrinsicTypes.KEY, new Type[]{SystemType.INSTANCE.getInstanceType(), PrimitiveType.INT32}, target, "load_key", new int[]{system, keyId}, false, false, false);
    }

    public static TIntrinsic createEncrypt(int buffer, int offset, int length, int key) {
        return new TIntrinsic(PrimitiveType.VOID, new Type[]{byteArray, PrimitiveType.INT32, PrimitiveType.INT32, IntrinsicTypes.KEY}, Registers.VOID_REG, "encrypt", new int[]{ buffer, offset, length, key }, true, true, false);
    }

    public static TIntrinsic createDecrypt(int buffer, int offset, int length, int key) {
        return new TIntrinsic(PrimitiveType.VOID, new Type[]{byteArray, PrimitiveType.INT32, PrimitiveType.INT32, IntrinsicTypes.KEY}, Registers.VOID_REG, "decrypt", new int[]{ buffer, offset, length, key }, true, true, false);
    }

    public static TIntrinsic createApplyMAC(int data, int dataSize, int offset, int key) {
        return new TIntrinsic(PrimitiveType.VOID, new Type[]{byteArray, PrimitiveType.INT32, PrimitiveType.INT32, IntrinsicTypes.KEY}, Registers.VOID_REG, "apply_mac", new int[]{ data, dataSize, offset, key }, true, true, false);
    }

    public static TIntrinsic createVerifyMAC(int data, int dataSize, int offset, int key) {
        return new TIntrinsic(PrimitiveType.VOID, new Type[]{byteArray, PrimitiveType.INT32, PrimitiveType.INT32, IntrinsicTypes.KEY}, Registers.VOID_REG, "verify_mac", new int[]{ data, dataSize, offset, key }, false, true, true);
    }

    public static TIntrinsic createEndpointGetId(int target, int endpoint) {
        return new TIntrinsic(PrimitiveType.INT32, new Type[]{IntrinsicTypes.ENDPOINT.getInstanceType()}, target, "endpoint_get_id", new int[]{ endpoint }, false, false, false);

    }
}
