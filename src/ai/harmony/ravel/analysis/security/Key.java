package ai.harmony.ravel.analysis.security;

import ai.harmony.ravel.primitives.Space;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gcampagn on 2/21/17.
 */ // A Key is represented a set of spaces that have some shared secret
public class Key {
    /**
     * The discriminator of the key, which is used to discriminate which
     * key should be used to decrypt/verify mac when a packet is received
     * <p>
     * This is usually the name of the originating space, but it need not
     * be.
     */
    public final Object keyDiscriminator;
    public final KeyType type;
    /**
     * The set of spaces who have access to this key
     */
    public final Set<Space> spaces = new HashSet<>();

    /**
     * The identifier of the key, which is a short integer used to build key
     * tables and load keys at runtimes.
     *
     * This MUST NOT be considered in equals and hashCode, or the way
     * we deduplicate keys will fail.
     */
    private int keyId;

    Key(Collection<Space> spaces, Object keyDiscriminator, KeyType type) {
        this.spaces.addAll(spaces);
        this.keyDiscriminator = keyDiscriminator;
        this.type = type;
    }

    void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public int getKeyId() {
        return keyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        return keyDiscriminator.equals(key.keyDiscriminator) && type == key.type && spaces.equals(key.spaces);
    }

    @Override
    public int hashCode() {
        int result = keyDiscriminator.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + spaces.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Key " + keyId + " (" + keyDiscriminator + ", " + type.toString() + ") " + spaces;
    }
}
