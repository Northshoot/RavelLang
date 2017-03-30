package edu.stanford.ravel.compiler;

import edu.stanford.antlr4.RavelParser;
import edu.stanford.ravel.compiler.types.NullType;
import edu.stanford.ravel.compiler.types.PrimitiveType;
import edu.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class ParserUtils {

    private ParserUtils() {}

    private static int parseInt(String str) {
        str = str.toLowerCase();

        if (str.startsWith("0x"))
            return Integer.parseInt(str.substring(2), 16);
        if (str.startsWith("0o"))
            return Integer.parseInt(str.substring(2), 8);
        if (str.startsWith("0b"))
            return Integer.parseInt(str.substring(2), 2);
        return Integer.parseInt(str);
    }

    public static Object literalToValue(RavelParser.LiteralContext ctx) {
        Object value;
        if (ctx.NONE() != null) {
            return NullType.NULL;
        } else if (ctx.number() != null) {
            if (ctx.number().integer() != null)
                value = parseInt(ctx.number().integer().getText());
            else
                value = Double.parseDouble(ctx.number().float_point().getText());
        } else if (ctx.boolean_rule() != null) {
            value = (ctx.boolean_rule().TRUE() != null);
        } else {
            value = ParserUtils.extractStringLiteral(ctx.STRING_LITERAL().getText());
        }
        return value;
    }

    public static Type typeFromLiteral(Object literal) {
        if (literal == NullType.NULL)
            return NullType.NULL_TYPE;
        if (literal instanceof Boolean)
            return PrimitiveType.BOOL;
        if (literal instanceof String)
            return PrimitiveType.STR;
        if (literal instanceof Double)
            return PrimitiveType.DOUBLE;
        if (literal instanceof Integer)
            return PrimitiveType.INT32;
        if (literal instanceof Byte)
            return PrimitiveType.BYTE;
        throw new AssertionError("Unexpected literal " + literal);
    }

    public static String extractStringLiteral(String input) {
        StringBuilder builder = new StringBuilder();

        // eat the first and the last quote
        for (int i = 1; i < input.length()-1; i++) {
            char c = input.charAt(i);
            if (c == '\\') {
                char next = input.charAt(i+1);
                switch (next) {
                    case 'n':
                        builder.append('\n');
                        break;
                    case 'r':
                        builder.append('\r');
                        break;
                    case 'f':
                        builder.append('\f');
                        break;
                    case 't':
                        builder.append('\t');
                        break;
                    case '0':
                        builder.append('\0');
                        break;
                    default:
                        builder.append(next);
                        break;
                }
                i++;
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    public static Object convertLiterals(Type tgtType, Type srcType, Object value) {
        switch ((PrimitiveType)tgtType) {
            case ANY:
                return null;

            case BOOL:
                if (srcType == PrimitiveType.ERROR_MSG)
                    return null;
                if (srcType == PrimitiveType.BOOL)
                    return (boolean)value;
                else if (srcType == PrimitiveType.BYTE)
                    return ((byte)value != 0);
                else if (srcType == PrimitiveType.INT32)
                    return ((int)value != 0);
                else if (srcType == PrimitiveType.DOUBLE)
                    return ((double)value != 0);
                else if (srcType == PrimitiveType.STR)
                    return (!((String)value).isEmpty());
                else
                    throw new AssertionError();
            case BYTE:
                if (srcType == PrimitiveType.BOOL)
                    return (byte)(((boolean)value) ? 1 : 0);
                else if (srcType == PrimitiveType.BYTE)
                    return (byte)value;
                else if (srcType == PrimitiveType.INT32)
                    return (byte)(int)value;
                else if (srcType == PrimitiveType.DOUBLE)
                    return (byte)(double)value;
                else if (srcType == PrimitiveType.STR)
                    return Byte.valueOf((String)value);
                else
                    throw new AssertionError();
            case INT32:
                if (srcType == PrimitiveType.BOOL)
                    return ((boolean)value) ? 1 : 0;
                else if (srcType == PrimitiveType.BYTE)
                    return (int)(byte)value;
                else if (srcType == PrimitiveType.INT32)
                    return (int)value;
                else if (srcType == PrimitiveType.DOUBLE)
                    return (int)(double)value;
                else if (srcType == PrimitiveType.STR)
                    return Integer.valueOf((String)value);
                else
                    throw new AssertionError();
            case DOUBLE:
                if (srcType == PrimitiveType.BOOL)
                    return ((boolean)value) ? 1.0 : 0.0;
                else if (srcType == PrimitiveType.INT32)
                    return (double)(int)value;
                else if (srcType == PrimitiveType.DOUBLE)
                    return (double)value;
                else if (srcType == PrimitiveType.STR)
                    return Double.valueOf((String)value);
                else
                    throw new AssertionError();

            case STR:
                return value.toString();
        }

        return null;
    }
}
