package org.stanford.ravel.compiler.types;

import java.util.Collection;

/**
 * Created by gcampagn on 1/20/17.
 */
public interface CompoundType extends Type {
    Collection<String> getMemberList();
    Type getMemberType(String member);
}
