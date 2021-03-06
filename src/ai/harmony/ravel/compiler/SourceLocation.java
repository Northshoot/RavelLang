package ai.harmony.ravel.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

/**
 * Created by gcampagn on 1/20/17.
 */
public class SourceLocation {
    public String fileName;
    public final int lineStart;
    public final int columnStart;
    public final int lineEnd;
    public final int columnEnd;

    public SourceLocation(ParserRuleContext ctx) {
        Token start = ctx.getStart();
        Token end = ctx.getStop();
        //TODO: fix line name for the error
        fileName = ctx.getStart().getTokenSource().getSourceName();
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
        return "file: " + this.fileName + " " + lineStart + ":" + columnStart;
    }
}
