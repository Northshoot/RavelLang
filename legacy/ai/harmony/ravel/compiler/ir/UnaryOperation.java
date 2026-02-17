package ai.harmony.ravel.compiler.ir;

import ai.harmony.ravel.compiler.types.PrimitiveType;
import ai.harmony.ravel.compiler.types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/23/17.
 */
public enum UnaryOperation {
    NEG("-"), PLUS("+"), BNOT("~"), NOT("!");

    private static final Map<String, UnaryOperation> symbolMap = new HashMap<>();

    private final String symbol;
    UnaryOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public boolean isNumeric() {
        return this != NOT;
    }
    public boolean isBitwise() {
        return this == BNOT;
    }

    static {
        for (UnaryOperation op : values())
            symbolMap.put(op.symbol, op);
    }

    static UnaryOperation forSymbol(String symbol) {
        return symbolMap.get(symbol);
    }

    public boolean isLegalType(Type type) {
        switch (this) {
            case NEG:
            case PLUS:
                return type == PrimitiveType.INT32 || type == PrimitiveType.DOUBLE;
            case BNOT:
                return type == PrimitiveType.INT32 || type == PrimitiveType.BYTE;
            case NOT:
                return type == PrimitiveType.BOOL;
            default:
                return false;
        }
    }
}
