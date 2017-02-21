package org.stanford.ravel.analysis.security;

/**
 * The set of supported operations on encrypted data
 *
 * Created by gcampagn on 2/21/17.
 */
public enum HomomorphicOperation {
    NONE,

    HOMO_ADD,
    HOMO_MUL,
    MULTIPARTY_ADD,

    INVALID;

    public static HomomorphicOperation meet(HomomorphicOperation m1, HomomorphicOperation m2) {
        if (m1 == m2)
            return m1;
        if (m1 == NONE)
            return m2;
        if (m2 == NONE)
            return m1;
        return INVALID;
    }

    public static HomomorphicOperation forPrimitive(SecurityLevel prim) {
        switch (prim) {
            case NONE:
            case VERIFY_MAC:
            case DECRYPT:
                // IMPORTANT NOTE: in the decrypt case, there is no homomorphic operation
                // on this stage (instead, there is a non-homomorphic full decryption happening)
                // so we return NONE here, not INVALID
                // this means that if one space creates the value, one space homomorphically
                // adds it and another space decrypts it the result is HOMO_ADD as it should be
                // but if one space creates the value, one space homomorphically adds it
                // and another space homomorphically multiplies it, we bail and return INVALID
                // causing all spaces to decrypt

                return NONE;

            case HOMO_ADD:
                return HOMO_ADD;
            case HOMO_MUL:
                return HOMO_MUL;
            case MULTIPARTY_ADD:
                return MULTIPARTY_ADD;

            default:
                return INVALID;
        }
    }
}
