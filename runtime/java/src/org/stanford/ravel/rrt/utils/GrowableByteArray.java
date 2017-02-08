package org.stanford.ravel.rrt.utils;

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
}
