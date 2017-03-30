package edu.stanford.ravel.analysis;

/**
 * A {@link java.util.function.BiConsumer} with 3 arguments.
 *
 * Created by gcampagn on 2/13/17.
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {
    void apply(A a, B b, C c);
}
