package org.stanford.ravel.rrt.utils;

import java.nio.ByteBuffer;

/**
 * Created by lauril on 1/25/17.
 */
public class ByteWork {
    /***
     * returns byte part of a byte array
     * @param bytes byte array
     * @param start starting index of byte array
     * @param end end index of byte array
     * @return byte[stop-end]
     */
    public static byte[] getBytes(byte[] bytes, int start, int end) {
        byte[] b = new byte[end - start];
        for (int i = 0; start < end; i++) {
            b[i] = bytes[start++];
        }
        return b;
    }

    public int unsignedIntFromByteArray(byte[] bytes) {
        int res = 0;
        for (int i = 0; i < bytes.length; i++) {
            res = res | ((bytes[i] & 0xff) << i * 8);
        }
        return res;
    }


    public static int convertTwoBytesToInt(byte[] bytes) {
        if (bytes.length != 2) throw new AssertionError("Expected 2 bytes");
        return (bytes[1] << 8) | (bytes[0] & 0xFF);
    }

    public static int convertFourBytesToInt(byte[] bytes) {
//            if (bytes.length !=4 ) throw new AssertionError("Expected 4 bytes");
//            return (bytes[3] << 24) | (bytes[2] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        //buffer.order(ByteOrder.LITTLE_ENDIAN);  // if you want little-endian
        return buffer.getInt();
    }

    public static double convertEightBytesToDouble(byte[] bytes) {
//            if (bytes.length !=4 ) throw new AssertionError("Expected 4 bytes");
//            return (bytes[3] << 24) | (bytes[2] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        //buffer.order(ByteOrder.LITTLE_ENDIAN);  // if you want little-endian
        return buffer.getDouble();
    }

    public static boolean convertOneByteToBool(byte[] bytes) {
        return bytes[0] != 0;
    }

    /***
     * Convert two unsigned bytes to int Litle Endian!
     * @param bytes
     * @return converted int
     */
    public static int convertTwoUnsignedBytesToInt(byte[] bytes) {
        if (bytes.length != 2) throw new AssertionError("Expected 2 bytes");
        return (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
    }

    /***
     * convert 4 unsigned bytes to long
     * @param bytes
     * @return converted long
     */
    public static int convertFourUnsignedBytesToInt(byte[] bytes) {
        if (bytes.length != 4) throw new AssertionError("Expected 4 bytes");
        return (bytes[0] & 0xFF) << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    /**
     * Solution by
     * http://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java#9855338
     */
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] getByteArray(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }

    public static byte[] getByteArray(double val) {
        return ByteBuffer.allocate(4).putDouble(val).array();
    }

    public static byte[] getByteArray(boolean val) {
        return ByteBuffer.allocate(4).put((byte) (val ? 1 : 0)).array();
    }
}
