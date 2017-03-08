package org.stanford.ravel.rrt.lang;

import org.stanford.ravel.rrt.Crypto;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.model.ModelRecord;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;
import org.stanford.ravel.rrt.utils.ByteWork;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Implementations for TIntrinsic calls
 *
 * Created by gcampagn on 2/21/17.
 */
public class Intrinsic {
    public static String endpoint_get_name(Endpoint ep) {
        return ep.getName();
    }

    public static byte read_record_id(ModelRecord record) {
        return (byte)record.index();
    }

    public static int array_length(Object[] array) {
        if (array == null)
            return 0;
        return array.length;
    }
    public static int array_length(byte[] array) {
        if (array == null)
            return 0;
        return array.length;
    }
    public static int array_length(int[] array) {
        if (array == null)
            return 0;
        return array.length;
    }
    public static int array_length(double[] array) {
        if (array == null)
            return 0;
        return array.length;
    }
    public static int array_length(boolean[] array) {
        if (array == null)
            return 0;
        return array.length;
    }

    public static int strlen(String str) {
        if (str == null)
            return 0;
        return str.length();
    }

    public static boolean extract_bool(byte[] data, int pos) {
        return ByteWork.convertOneByteToBool(ByteWork.getBytes(data, pos, pos+1));
    }

    public static byte extract_byte(byte[] data, int pos) {
        return data[pos];
    }

    public static int extract_uint16(byte[] data, int pos) {
        return ByteWork.convertTwoUnsignedBytesToInt(ByteWork.getBytes(data, pos, pos+2));
    }

    public static int extract_int32(byte[] data, int pos) {
        return ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, pos, pos+4));
    }

    public static Error extract_error_msg(byte[] data, int pos) {
        return ByteWork.convertFourBytesToError(ByteWork.getBytes(data, pos, pos+4));
    }

    public static Date extract_timestamp(byte[] data, int pos) {
        return ByteWork.convertFourBytesToDate(ByteWork.getBytes(data, pos, pos+4));
    }

    public static String extract_str(byte[] data, int pos, int size) {
        return ByteWork.convertBytesToString(ByteWork.getBytes(data, pos, pos+size));
    }

    public static void write_int32(byte[] data, int pos, int value) {
        byte[] bytes = ByteWork.getByteArray(value);
        System.arraycopy(bytes, 0, data, pos, bytes.length);
    }

    public static void write_byte(byte[] data, int pos, byte value) {
        data[pos] = value;
    }

    public static void apply_mac(byte[] data, int endofdata, int writeOffset, Key key) {
        Crypto.apply_mac(data, endofdata, writeOffset, key);
    }
    public static void verify_mac(byte[] data, int endofdata, int macoffset, Key key) {
        Crypto.verify_mac(data, endofdata, macoffset, key);
    }

    public static void encrypt(byte[] data, int offset, int length, Key key) {
        Crypto.encrypt(data, offset, length, key);
    }

    public static void decrypt(byte[] data, int offset, int length, Key key) {
        Crypto.decrypt(data, offset, length, key);
    }

    public static Key load_key(DispatcherAPI dispatcher, int keyId) {
        Key key = dispatcher.getKeyProvider().loadKey(keyId);
        if (key == null)
            throw new SecurityException("Invalid key identifier " + keyId);
        return key;
    }

    public static void array_copy(byte[] tgt, byte[] source, int tgtOffset, int srcOffset, int size) {
        System.arraycopy(source, srcOffset, tgt, tgtOffset, size);
    }

    private static final SecureRandom randomSource = new SecureRandom();
    public static void array_fill_random(byte[] buffer, int offset, int length) {
        byte[] random = new byte[length];
        randomSource.nextBytes(random);
        System.arraycopy(random, 0, buffer, offset, length);
    }
}
