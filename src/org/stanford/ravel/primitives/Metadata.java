package org.stanford.ravel.primitives;

/**
 * Annotations that can be applied to variables, event handlers, controllers, etc
 * by the analysis pass
 *
 * Created by gcampagn on 2/10/17.
 */
public enum Metadata {
    /**
     * Who (set of instantiated controllers) creates the value in this variable
     */
    CREATOR
}
