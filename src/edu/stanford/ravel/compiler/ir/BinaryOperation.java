package edu.stanford.ravel.compiler.ir;

import edu.stanford.ravel.compiler.types.PrimitiveType;
import edu.stanford.ravel.compiler.types.Type;

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

    public boolean isLegalType(Type type) {
        if (type == PrimitiveType.ERROR)
            return true;

        switch (this) {
            case POW:
            case DIV:
                return type == PrimitiveType.DOUBLE;
            case MUL:
            case IDIV:
            case MOD:
            case SUB:
                return type == PrimitiveType.DOUBLE || type == PrimitiveType.INT32;
            case ADD:
                return type == PrimitiveType.DOUBLE || type == PrimitiveType.INT32 || type == PrimitiveType.STR;
            case LSHIFT:
                return type == PrimitiveType.BYTE || type == PrimitiveType.INT32;
            case RSHIFT:
                return type == PrimitiveType.BYTE || type == PrimitiveType.INT32;
            case AND:
                return type == PrimitiveType.BYTE || type == PrimitiveType.INT32;
            case OR:
                return type == PrimitiveType.BYTE || type == PrimitiveType.INT32;
            case XOR:
                return type == PrimitiveType.BYTE || type == PrimitiveType.INT32;
            default:
                return false;
        }
    }
}
