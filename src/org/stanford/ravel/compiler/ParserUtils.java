package org.stanford.ravel.compiler;

/**
 * Created by gcampagn on 1/23/17.
 */
public class ParserUtils {
    private ParserUtils() {}

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
