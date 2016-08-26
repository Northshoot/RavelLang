package ai.harmony.ravel.error;

import ai.harmony.ravel.antlr4.RavelLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.LexerNoViableAltException;


/**
 * Created by lauril on 8/25/16.
 */
public class ExitLexer extends RavelLexer {
    public ExitLexer(CharStream input) { super(input); }
    public void recover(LexerNoViableAltException e) {

        throw new RuntimeException(e); // Bail out
    }
}