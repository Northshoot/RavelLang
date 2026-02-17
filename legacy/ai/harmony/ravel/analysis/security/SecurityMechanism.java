package ai.harmony.ravel.analysis.security;

/**
 * Metadata about security mechanism.
 *
 * This is where the crypto choices happen.
 *
 * Created by gcampagn on 2/27/17.
 */
public class SecurityMechanism {
    public static final String MAC_ALGORITHM = "HmacSHA256";
    public static final String KEY_ALGORITHM = "AES";
    public static final String ENCRYPTION_ALGORITHM = "AES/CTR/NoPadding";

    public int getEncryptionIVSize() {
        return 8;
    }

    public int getEncryptionBlockSize() {
        return 16;
    }

    public boolean mustAlignToBlockSize() {
        return false;
    }

    public int getEncryptionKeySize() {
        return 16;
    }

    /**
     * Following RFC 2104 "HMAC: Keyed-Hashing for Message Authentication",
     * the recommended key size is the block size of the internal hash
     * function.
     *
     * For SHA-256, this is 512 bits, or 64 bytes
     *
     * @return the size of a MAC key in the chosen mechanism (HMAC-SHA256)
     */
    public int getMACKeySize() {
        return 64;
    }

    /**
     * Our MAC of choice is HMAC, which means the size of the MAC is
     * the output size of the hash function.
     * We truncate it, following RFC 2104 Section 5, to half of the output,
     * for space efficency.
     *
     * For SHA-256, this is 256 bits, or 32 bytes, which we truncate down
     * to 16.
     *
     * @return the size of a MAC in the chosen mechanism (HMAC-SHA256)
     */
    public int getMACSize() {
        return 16;
    }
}
