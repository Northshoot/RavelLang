package org.stanford.ravel.api.lang;

import org.stanford.ravel.compiler.types.Type;

/**
 * Convert a type to a string. This is a delegate interface
 * used by STIRTranslator
 *
 * Created by gcampagn on 1/29/17.
 */
@FunctionalInterface
public interface TypeFormatter {
    String toNativeType(Type type);
}
