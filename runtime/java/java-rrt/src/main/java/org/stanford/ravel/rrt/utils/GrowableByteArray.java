package org.stanford.ravel.rrt.utils;

import org.stanford.ravel.rrt.tiers.Error;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

/**
 * As the name suggests, a byte array that can be written into and
 * automatically grows
 *
 * Similar to a ByteArrayOutputStream, except that it doesn't throw
 * IOException in random places
 *
 * Created by gcampagn on 2/8/17.
 */
public class GrowableByteArray {
    private byte[] storage;
    private int length;

    public GrowableByteArray() {
        this(32);
    }

    public GrowableByteArray(int initialSize) {
        storage = new byte[initialSize];
        length = 0;
    }

    public int size() {
        return length;
    }
    public int capacity() {
        return storage.length;
    }

    private void grow(int until) {
        byte[] newStorage = new byte[Math.max(until, storage.length * 3 / 2)];
        System.arraycopy(storage, 0, newStorage, 0, storage.length);
        storage = newStorage;
    }

    public void write(byte[] bytes) {
        write(bytes, 0, bytes.length);
    }

    public void write(byte[] bytes, int off, int srclength) {
        if (length + srclength < storage.length)
            grow(length + srclength);
        System.arraycopy(bytes, off, storage, length, srclength);
        length += srclength;
        assert length <= storage.length;
    }

    public byte[] toByteArray() {
        byte[] array = new byte[length];
        System.arraycopy(storage, 0, array, 0, length);
        return array;
    }


    // The API called by Ravel code
    public static GrowableByteArray create() {
        return new GrowableByteArray();
    }

    public void write_int32(int v) {
        write(ByteWork.getByteArray(v));
    }
    public void write_uint16(int v) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short)v);
        write(buffer.array());
    }
    public void write_error_msg(Error error) {
        if (error == null)
            error = Error.SUCCESS;
        write_int32(error.ordinal());
    }
    public void write_str(String str) {
        if (str == null)
            write_uint16(0);
        else
            write(ByteWork.getByteArray(str));
    }
    public void write_byte(byte v) {
        write(new byte[]{v});
    }
    public void write_timestamp(Date v) {
        write(ByteWork.getByteArray(v));
    }
    public void write_double(double v) {
        write(ByteWork.getByteArray(v));
    }
    public void write_bool(boolean b) {
        write_byte((byte) (b ? 1 : 0));
    }
    public void write_byte_array(byte[] array) {
        write(array);
    }
}
