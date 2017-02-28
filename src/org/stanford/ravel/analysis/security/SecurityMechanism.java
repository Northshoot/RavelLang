package org.stanford.ravel.analysis.security;

/**
 * Metadata about security mechanism.
 *
 * This is where the crypto choices happen.
 *
 * Created by gcampagn on 2/27/17.
 */
public class SecurityMechanism {
    public int getEncryptionIVSize() {
        return 8;
    }

    public int getEncryptionBlockSize() {
        return 16;
    }

    public int getEncryptionKeySize() {
        return 16;
    }

    public int getMACKeySize() {
        return 16;
    }

    // 80 bit mac
    public int getMACSize() {
        return 80/8;
    }
}
