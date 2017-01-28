package org.stanford.ravel.compiler.ir;

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
}
