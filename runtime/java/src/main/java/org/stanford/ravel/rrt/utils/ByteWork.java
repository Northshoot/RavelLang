package org.stanford.ravel.rrt.utils;

import org.stanford.ravel.rrt.tiers.Error;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Date;

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

    public static Error convertFourBytesToError(byte[] bytes) {
        return Error.values()[convertFourBytesToInt(bytes)];
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
    public static byte convertOneByteToByte(byte[] bytes) {
        return bytes[0];
    }

    public static String convertBytesToString(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // UTF-8 is guaranteed to be supported
            throw new RuntimeException(e);
        }
    }

    /***
     * Convert two unsigned bytes to int Litle Endian!
     *
     * This is used by string and array length serialization
     *
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

    private static int charToNibble(char c) {
        if (c >= 'A' && c <= 'F')
            return 0xA + (c - 'A');
        if (c >= 'a' && c <= 'f')
            return 0xa + (c - 'a');
        if (c >= '0' && c <= '9')
            return (c - '0');
        throw new NumberFormatException("Invalid character " + c);
    }

    public static byte[] bytesFromHex(CharSequence sequence) {
        byte[] bytes = new byte[sequence.length()/2];

        for (int i = 0; i < bytes.length; i++) {
            char c1 = sequence.charAt(2 * i);
            char c2 = sequence.charAt(2 * i + 1);

            bytes[i] = (byte)(charToNibble(c1) << 4 | charToNibble(c2));
        }

        return bytes;
    }

    public static byte[] getLengthByteArray(int length) {
        byte[] buffer = new byte[2];
        // careful, this little endian, because it matches convertTwoUnsignedBytesToInt
        buffer[0] = (byte) (length & 0xFF);
        buffer[1] = (byte) ((length >> 8) & 0xFF);
        return buffer;
    }

    public static byte[] getByteArray(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }

    public static byte[] getByteArray(double val) {
        return ByteBuffer.allocate(8).putDouble(val).array();
    }

    public static byte[] getByteArray(boolean val) {
        return ByteBuffer.allocate(4).put((byte) (val ? 1 : 0)).array();
    }

    public static byte[] getByteArray(String val) {
        byte[] stringBytes = val.getBytes(Charset.forName("UTF-8"));
        if (stringBytes.length > 65535)
            throw new RuntimeException("String is too long");
        byte[] buffer = new byte[2 + stringBytes.length];
        // careful, this little endian, because it matches convertTwoUnsignedBytesToInt
        buffer[0] = (byte) (stringBytes.length & 0xFF);
        buffer[1] = (byte) ((stringBytes.length >> 8) & 0xFF);
        System.arraycopy(stringBytes, 0, buffer, 2, stringBytes.length);
        return buffer;
    }

    public static byte[] getByteArray(Error e) {
        return getByteArray(e.ordinal());
    }

    public static byte[] getByteArray(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(2 + bytes.length);
        if (bytes.length > 65535)
            throw new RuntimeException("byte array is too long");
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short)bytes.length);
        buffer.put(bytes);
        return buffer.array();
    }

    public static Date convertFourBytesToDate(byte[] bytes) {
        int timestamp = convertFourBytesToInt(bytes);
        return new Date(timestamp*1000);
    }

    public static byte[] getByteArray(Date time) {
        return getByteArray((int)(time.getTime()/1000));
    }
}
