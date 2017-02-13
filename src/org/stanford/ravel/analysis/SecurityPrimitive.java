package org.stanford.ravel.analysis;

/**
 * The available and understood security primitives / security levels,
 * applied to a model field or set of model fields.
 *
 * Like {@link Operation}, this forms a lattice, in which the minimum
 * element is {@link SecurityPrimitive#NONE}, and the maxmimum element
 * is {@link SecurityPrimitive#DECRYPT}. See {@link SecurityPrimitive#meet}
 * for details
 *
 * Created by gcampagn on 2/13/17.
 */
public enum SecurityPrimitive {
    /**
     * Nothing to do with this field/sets of fields
     */
    NONE,

    /**
     * Verify the MAC, copy the field around, but don't decrypt it
     */
    VERIFY_MAC,

    /**
     * Homomorphically add a constant (after verifying MAC)
     */
    HOMO_ADD_CONSTANT,

    /**
     * Homomorphically add to another value encrypted by the same
     * creator
     */
    HOMO_ADD,

    /**
     * Homomorphically add to another value encrypted by a different
     * party
     */
    MULTIPARTY_ADD,

    /**
     * Decrypt fully (after verifying MAC)
     */
    DECRYPT;

    public static SecurityPrimitive meet(SecurityPrimitive sp1, SecurityPrimitive sp2) {
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
}
