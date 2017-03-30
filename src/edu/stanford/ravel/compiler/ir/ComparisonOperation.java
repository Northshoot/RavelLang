package edu.stanford.ravel.compiler.ir;

import edu.stanford.ravel.compiler.types.PrimitiveType;
import edu.stanford.ravel.compiler.types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/23/17.
 */
public enum ComparisonOperation {
    GT(">"), LT("<"), EQUAL("=="), LE("<="), GE(">="), NOTEQUAL("!=");

    private static final Map<String, ComparisonOperation> symbolMap = new HashMap<>();

    private final String symbol;
    ComparisonOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    static {
        for (ComparisonOperation op : values())
            symbolMap.put(op.symbol, op);
    }

    static ComparisonOperation forSymbol(String symbol) {
        assert symbolMap.containsKey(symbol);
        return symbolMap.get(symbol);
    }

    public boolean isLegalType(Type type) {
        switch (this) {
            case EQUAL:
            case NOTEQUAL:
                return true;

            case GT:
            case LT:
            case LE:
            case GE:
                return type == PrimitiveType.BYTE || type == PrimitiveType.INT32 || type == PrimitiveType.DOUBLE ||
                        type == PrimitiveType.STR;

            default:
                return false;
        }
    }
}
