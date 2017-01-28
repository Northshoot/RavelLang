package org.stanford.ravel.compiler.ir;

/**
 * Created by gcampagn on 1/23/17.
 */
public class Registers {
    public final static int UNSET_REG = 0;
    public final static int VOID_REG = -1;
    public final static int ERROR_REG = -2;

    private Registers() {}

    public static boolean isError(int self) {
        return self == ERROR_REG;
    }
    public static boolean isVoid(int self) {
        return self == VOID_REG;
    }
    public static boolean isNormal(int self) {
        return self >= 1;
    }
}
