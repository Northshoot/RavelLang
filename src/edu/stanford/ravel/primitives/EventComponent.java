package edu.stanford.ravel.primitives;

/**
 * Something that holds events. Could be a model, a source or a sink
 *
 * Created by gcampagn on 1/30/17.
 */
public interface EventComponent {
    void addLinkedEvent(LinkedEvent event);

    boolean hasMultipleInstances();
}
