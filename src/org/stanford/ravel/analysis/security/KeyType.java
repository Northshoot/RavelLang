package org.stanford.ravel.analysis.security;

/**
 * What type of key this is
 */
public enum KeyType {
    /**
     * A symmetric key that supports encryption
     */
    SYMMETRIC_ENCRYPT,

    /**
     * A symmetric key that supports MACs
     */
    SYMMETRIC_MAC,

    /**
     * A symmetric key that supports Authenticated Encryption (encrypt-then-mac)
     */
    SYMMETRIC_AUTH_ENC,

    /**
     * The public part of an asymmetric key that supports homomorphic addition
     * (eg Paillier public)
     */
    HOMO_ADD_PUBLIC,

    /**
     * The secret part of an asymmetric key that supports homomorphic addition
     * (eg Paillier secret)
     */
    HOMO_ADD_SECRET,

    /**
     * The public part of an asymmetric key that supports homomorphic multiplication
     * (eg ElGamal or RSA)
     */
    HOMO_MUL_PUBLIC,

    /**
     * The secret part of an asymmetric key that supports homomorphic multiplication
     * (eg ElGamal or RSA)
     */
    HOMO_MUL_SECRET
}
