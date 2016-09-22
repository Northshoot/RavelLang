package ai.harmony.ravel.error;

import ai.harmony.antlr4.RavelLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;


/**
 * Created by lauril on 8/25/16.
 */
public class NotImplementedException extends Exception {
    public NotImplementedException(String m) { super("Method " + m + " not implemented!"); }

}