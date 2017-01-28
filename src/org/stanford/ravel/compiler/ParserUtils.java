package org.stanford.ravel.compiler;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class ParserUtils {
    private ParserUtils() {}

    public static Object literalToValue(RavelParser.LiteralContext ctx) {
        Object value;
        if (ctx.number() != null) {
            if (ctx.number().integer() != null)
                value = Integer.parseInt(ctx.number().integer().getText());
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
        if (literal instanceof Boolean)
            return PrimitiveType.BOOL;
        if (literal instanceof String)
            return PrimitiveType.STR;
        if (literal instanceof Double)
            return PrimitiveType.DOUBLE;
        if (literal instanceof Integer)
            return PrimitiveType.INT32;
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
}
