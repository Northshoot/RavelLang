package org.stanford.ravel.compiler.types;

/**
 * Created by gcampagn on 1/20/17.
 */
public enum PrimitiveType implements Type {
    // special types

    // the return type of a function that returns void
    VOID,

    // the type of an expression whose type we haven't determined yet
    // (before type inference/type resolution)
    ANY,

    // the type of an expression which does not type check
    ERROR {
        @Override
        public boolean isAssignable(Type type) {
            return true;
        }
    },

    // regular types
    BOOL,
    BYTE {
        @Override
        public boolean isAssignable(Type type) {
            return type == BYTE || type == BOOL || type == ERROR;
        }
    },
    INT32 {
        @Override
        public boolean isAssignable(Type type) {
            return type == INT32 || type == BYTE || type == BOOL || type == ERROR;
        }
    },
    DOUBLE {
        @Override
        public boolean isAssignable(Type type) {
            return type == DOUBLE || type == BYTE || type == INT32 || type == BOOL || type == ERROR;
        }
    },
    STR,
    ERROR_MSG,
    DATE,
    DATE_TIME,
    TIMESTAMP;

    public String getName() {
        return name().toLowerCase();
    }

    public boolean isValid() {
        return this != ANY && this != ERROR;
    }
}
