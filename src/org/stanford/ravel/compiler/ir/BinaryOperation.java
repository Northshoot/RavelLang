package org.stanford.ravel.compiler.ir;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/23/17.
 */
public enum BinaryOperation {
    POW("**"),
    MUL("*"), DIV("/"), IDIV("//"), MOD("%"),
    ADD("+"), SUB("-"),
    LSHIFT("<<"), RSHIFT(">>"),
    AND("&"), OR("|"), XOR("^");

    private static final Map<String, BinaryOperation> symbolMap = new HashMap<>();

    private final String symbol;
    BinaryOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public boolean isNumeric() {
        return this != ADD;
    }
    public boolean isBitwise() {
        return this == LSHIFT || this == RSHIFT || this == AND || this == OR || this == XOR;
    }

    static {
        for (BinaryOperation op : values())
            symbolMap.put(op.symbol, op);
    }

    static BinaryOperation forSymbol(String symbol) {
        assert symbolMap.containsKey(symbol);
        return symbolMap.get(symbol);
    }
}
