package org.stanford.ravel.compiler.ir;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/23/17.
 */
public enum ComparisonOperation {
    GT(">"), LT(">"), EQUAL("=="), LE("<="), GE(">="), NOTEQUAL("!=");

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
        return symbolMap.get(symbol);
    }
}
