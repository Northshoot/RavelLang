package org.stanford.ravel.compiler.types;

/**
 * Created by gcampagn on 1/20/17.
 */
public enum PrimitiveType implements Type {
    VOID,
    ANY,
    ERROR {
        @Override
        public boolean isAssignable(Type type) {
            return true;
        }
    },
    BOOL,
    INT32 {
        @Override
        public boolean isAssignable(Type type) {
            return type == INT32 || type == BOOL || type == ERROR;
        }
    },
    DOUBLE {
        @Override
        public boolean isAssignable(Type type) {
            return type == DOUBLE || type == INT32 || type == BOOL || type == ERROR;
        }
    },
    STR;

    public String getName() {
        return name().toLowerCase();
    }

    public boolean isValid() {
        return this != ANY && this != ERROR;
    }
}
