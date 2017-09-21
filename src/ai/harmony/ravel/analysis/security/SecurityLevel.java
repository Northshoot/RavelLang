package ai.harmony.ravel.analysis.security;

import ai.harmony.ravel.analysis.Operation;

/**
 * The available and understood security primitives / security levels,
 * applied to a model field or set of model fields.
 *
 * Like {@link Operation}, this forms a lattice, in which the minimum
 * element is {@link SecurityLevel#NONE}, and the maxmimum element
 * is {@link SecurityLevel#DECRYPT}. See {@link SecurityLevel#meet}
 * for details
 *
 * Created by gcampagn on 2/13/17.
 */
public enum SecurityLevel {
    /**
     * Nothing to do with this field/sets of fields
     */
    NONE,

    /**
     * Verify the MAC, copy the field around, but don't decrypt it
     */
    VERIFY_MAC,

    /**
     * Homomorphically add to another value encrypted by the same
     * creator
     */
    HOMO_ADD,

    /**
     * Homomorphically multiply to another (integer) value encrypted by the same
     * creator
     */
    HOMO_MUL,

    /**
     * Homomorphically add to another value encrypted by a different
     * party
     */
    MULTIPARTY_ADD,

    /**
     * Decrypt fully (after verifying MAC)
     */
    DECRYPT;

    public static SecurityLevel meet(SecurityLevel sp1, SecurityLevel sp2) {
        // equal elements compare equals
        if (sp1 == sp2)
            return sp1;

        if (sp1 == null)
            sp1 = NONE;
        if (sp2 == null)
            sp2 = NONE;

        // identity element
        if (sp1 == NONE)
            return sp2;
        if (sp2 == NONE)
            return sp1;

        // zero element
        if (sp1 == DECRYPT)
            return DECRYPT;
        if (sp2 == DECRYPT)
            return DECRYPT;

        // verify mac is required by any other operation
        if (sp1 == VERIFY_MAC)
            return sp2;
        if (sp2 == VERIFY_MAC)
            return sp1;

        // any other operation decays to full decryption
        return DECRYPT;
    }

    public boolean requiresVerifyMac() {
        return this != NONE;
    }
}
