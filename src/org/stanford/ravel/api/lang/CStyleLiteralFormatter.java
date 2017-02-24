package org.stanford.ravel.api.lang;

/**
 * A literal formatter for languages that follow Java/C style
 * syntax conventions
 *
 * Created by gcampagn on 1/29/17.
 */
public class CStyleLiteralFormatter implements LiteralFormatter {
    private static String stringEscape(String str) {
        StringBuilder builder = new StringBuilder();

        builder.append('"');
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\t':
                    builder.append("\\t");
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\'':
                    builder.append("\\'");
                    break;
                case '"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                default:
                    if (!Character.isISOControl(c) && c < 128) { // ASCII
                        builder.append(c);
                    } else {
                        builder.append("\\u");
                        String hex = Integer.toHexString(c);
                        for (int j = 0; j < 4 - hex.length(); j++)
                            builder.append('0');
                        builder.append(hex);
                    }
            }
        }
        builder.append('"');

        return builder.toString();
    }

    @Override
    public String toLiteral(Object o) {
        if (o instanceof String)
            return stringEscape((String)o);
        else // Boolean, Integer, Double, Reference
            return o.toString();
    }
}
