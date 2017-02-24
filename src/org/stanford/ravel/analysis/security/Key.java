package org.stanford.ravel.analysis.security;

import org.stanford.ravel.primitives.Space;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gcampagn on 2/21/17.
 */ // A Key is represented a set of spaces that have some shared secret
public class Key {
    /**
     * The identifier of the key, which is used to discriminate which
     * key should be used to decrypt/verify mac when a packet is received
     * <p>
     * This is usually the name of the originating space, but it need not
     * be.
     */
    public final Object keyId;
    public final KeyType type;
    /**
     * The set of spaces who have access to this key
     */
    public final Set<Space> spaces = new HashSet<>();

    Key(Collection<Space> spaces, Object keyId, KeyType type) {
        this.spaces.addAll(spaces);
        this.keyId = keyId;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        return keyId.equals(key.keyId) && type == key.type && spaces.equals(key.spaces);
    }

    @Override
    public int hashCode() {
        int result = keyId.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + spaces.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Key " + keyId + " (" + type.toString() + ") " + spaces;
    }
}
