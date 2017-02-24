package org.stanford.ravel.analysis;

/**
 * An abstraction over what a controller
 * can do to a model field.
 *
 * Operation forms a lattice, with the minimum
 * element being {@link Operation#NONE} (which means the
 * field is not even touched (and does not need to MACed) and the maximum
 * element being {@link Operation#ANY} (which means we cannot
 * determine what operations are being done, and so we need to
 * fully decrypt). See {@link Operation#meet} for details.
 *
 * Created by gcampagn on 2/13/17.
 */
public enum Operation {
    /**
     * The value is not being read at all (no MAC needed)
     */
    NONE,

    /**
     * The value is being read and copied around (MAC needed, but no decryption)
     */
    MOVE,

    /**
     * The value is an integer being added to another integer
     */
    IADD,

    /**
     * The value is an integer being multiplied to another integer
     */
    IMUL,

    /**
     * The value is being concatenated to another value
     */
    CONCAT,

    /**
     * The value is an (encrypted) array being indexed by another (plaintext) value
     *
     * This requires moving the IV around, but is otherwise a move
     *
     * FIXME is this true?
     */
    INDEX_LOAD,

    /**
     * The value is an (encrypted) array in which an (encrypted) value is being stored.
     * The encrypted value itself will be tagged with {@link Operation#MOVE}.
     *
     * This requires moving the IV around, but is otherwise a move
     *
     * FIXME is this true?
     */
    INDEX_STORE,

    /**
     * The value is being manipulated in any other way (including passing to an
     * external function, eg printing).
     *
     * This needs full decryption.
     */
    ANY;

    public static Operation meet(Operation op1, Operation op2) {
        // equal elements always compare equal
        if (op1 == op2)
            return op1;

        if (op1 == null)
            op1 = NONE;
        if (op2 == null)
            op2 = NONE;

        // the minimum element is the identity
        if (op1 == NONE)
            return op2;
        if (op2 == NONE)
            return op1;
        // the maximum element is the zero
        if (op1 == ANY)
            return ANY;
        if (op2 == ANY)
            return ANY;

        // move is smaller than any other element
        if (op1 == MOVE)
            return op2;
        if (op2 == MOVE)
            return op1;

        // the other elements are incomparable, and their sup is ANY
        return ANY;
    }
}
