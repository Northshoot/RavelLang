package org.stanford.ravel.rrt;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by lauril on 1/30/17.
 */
public class QueueArray<T> extends AbstractQueue<T> {

    private Object[] arr;

    private int total, first, next;

    public QueueArray()
    {
        arr = new Object[2];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int i = first;

            @Override
            public boolean hasNext() {
                return i != next;
            }

            @Override
            public T next() {
                T el = get(i);
                i = (i+1) % arr.length;
                return el;
            }
        };
    }

    @Override
    public int size() {
        return total;
    }

    private void resize(int capacity)
    {
        Object[] tmp = new Object[capacity];
        for (int i = 0; i < total; i++)
            tmp[i] = arr[(first + i) % arr.length];

        arr = tmp;
        first = 0;
        next = total;
    }

    @SuppressWarnings("unchecked")
    private T get(int idx) {
        return (T)arr[idx];
    }

    @Override
    public T poll()
    {
        if (total == 0)
            return null;
        T ele = get(first);
        arr[first] = null;
        if (++first == arr.length) first = 0;
        if (--total > 0 && total == arr.length / 4) resize(arr.length / 2);
        return ele;
    }

    @Override
    public String toString()
    {
        return java.util.Arrays.toString(arr);
    }

    @Override
    public boolean offer(T t) {
        if (arr.length == total) resize(arr.length * 2);
        arr[next++] = t;
        if (next == arr.length) next = 0;
        total++;
        return true;
    }

    @Override
    public T peek() {
        if (total == 0)
            return null;
        return get(first);
    }
}
