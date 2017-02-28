package org.stanford.ravel.rrt.lang;

import org.stanford.ravel.rrt.tiers.Error;
import org.stanford.ravel.rrt.utils.ByteWork;

import java.util.Arrays;
import java.util.Date;

/**
 * Implementations for TIntrinsic calls
 *
 * Created by gcampagn on 2/21/17.
 */
public class Intrinsic {
    public static int array_length(Object[] array) {
        return array.length;
    }
    public static int array_length(byte[] array) {
        return array.length;
    }
    public static int array_length(int[] array) {
        return array.length;
    }
    public static int array_length(double[] array) {
        return array.length;
    }
    public static int array_length(boolean[] array) {
        return array.length;
    }

    public static int strlen(String str) {
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

    public static void apply_mac(byte[] data, int endofdata, int writeOffset, byte[] key) {
        // TODO
        Arrays.fill(data, 0, endofdata, (byte)0);
    }
    public static void verify_mac(byte[] data, int endofdata, int macoffset, byte[] key) {
        // TODO
    }

    public static void encrypt(byte[] data, int offset, int length, byte[] key) {
        // TODO
    }

    public static void decrypt(byte[] data, int offset, int length, byte[] key) {
        // TODO
    }

    public static byte[] load_key(int keyId) {
        // TODO
        return null;
    }

    public static void array_copy(byte[] tgt, byte[] source, int tgtOffset, int srcOffset, int size) {
        System.arraycopy(source, srcOffset, tgt, tgtOffset, size);
    }
}
