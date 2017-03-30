package edu.stanford.ravel.analysis.security;

/**
 * The actual, concrete, low-level security primitives
 *
 * Created by gcampagn on 2/21/17.
 */
public enum SecurityPrimitive {
    APPLY_MAC,
    VERIFY_MAC,
    ENCRYPT,
    DECRYPT
};
