package org.stanford.ravel.compiler.types;

/**
 * Created by gcampagn on 1/20/17.
 */
public enum PrimitiveType implements Type {
    // special types

    // the return type of a function that returns void
    VOID(0),

    // the type of an expression whose type we haven't determined yet
    // (before type inference/type resolution)
    ANY(-1) {
        @Override
        public boolean isAssignable(Type type) {
            // FIXME FIXME FIXME THIS BREAKS THE TYPE SYSTEM
            return true;
        }
    },

    // the type of an expression which does not type check
    ERROR(-1) {
        @Override
        public boolean isAssignable(Type type) {
            return true;
        }
    },

    // regular types
    BOOL(1) {
        @Override
        public boolean isAssignable(Type type) {
            // error messages can be converted to bool to check if
            // error is set
            return type == BOOL || type == ERROR_MSG || type == ERROR;
        }
    },
    BYTE(1) {
        @Override
        public boolean isAssignable(Type type) {
            return type == BYTE || type == BOOL || type == ERROR;
        }
    },
    INT32(4) {
        @Override
        public boolean isAssignable(Type type) {
            return type == INT32 || type == BYTE || type == BOOL || type == ERROR;
        }
    },
    DOUBLE(8) {
        @Override
        public boolean isAssignable(Type type) {
            return type == DOUBLE || type == BYTE || type == INT32 || type == BOOL || type == ERROR;
        }
    },
    STR(-1),
    ERROR_MSG(4),
    TIMESTAMP(4);

    private final int size;

    private PrimitiveType(int size) {
        this.size = size;
    }

    @Override
    public int getSerializedSize() {
        return size;
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }

    public boolean isValid() {
        return this != ANY && this != ERROR;
    }
}
