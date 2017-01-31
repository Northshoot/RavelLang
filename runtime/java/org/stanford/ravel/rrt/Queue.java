package org.stanford.ravel.rrt;

/**
 * Created by lauril on 1/30/17.
 */
interface Queue<T> {
    Queue<T> enqueue(T ele);
    T dequeue();
}
