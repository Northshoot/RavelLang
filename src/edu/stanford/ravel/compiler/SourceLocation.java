package edu.stanford.ravel.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

/**
 * Created by gcampagn on 1/20/17.
 */
public class SourceLocation {
    public final int lineStart;
    public final int columnStart;
    public final int lineEnd;
    public final int columnEnd;

    public SourceLocation(ParserRuleContext ctx) {
        Token start = ctx.getStart();
        Token end = ctx.getStop();

        lineStart = start.getLine();
        columnStart = start.getCharPositionInLine();
        lineEnd = end.getLine();
        columnEnd = end.getCharPositionInLine();
    }

    public SourceLocation(int line, int column) {
        lineStart = lineEnd = line;
        columnStart = columnEnd = column;
    }

    public String toString() {
        return lineStart + ":" + columnStart;
    }
}
