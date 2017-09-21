package ai.harmony.ravel.compiler.ir;

/**
 * Created by gcampagn on 1/23/17.
 */
public class Registers {
    // special/non existing registers
    public final static int UNSET_REG = 0;
    public final static int VOID_REG = -1;
    public final static int ERROR_REG = -2;

    // register reserved for return values
    public final static int RETURN_REG = 1;

    // register reserved for event parameters
    public final static int SELF_REG = 2;

    // first general purpose register
    public final static int FIRST_GP_REG = 3;

    private Registers() {}

    public static boolean isNormal(int self) {
        return self >= RETURN_REG;
    }
}
