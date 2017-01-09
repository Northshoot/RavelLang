package org.stanford.ravel.error;

import org.antlr.v4.runtime.*;

/**
 * Created by lauril on 8/25/16.
 */
public class ExitParserStrategy extends DefaultErrorStrategy {
    /** Instead of recovering from exception e, rethrow it wrapped
     *  in a generic RuntimeException so it is not caught by the
     *  rule function catches.  Exception e is the "cause" of the
     *  RuntimeException.
     */

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        throw new RuntimeException(e);
    }

    /** Make sure we don't attempt to recover inline; if the parser
     *  successfully recovers, it won't throw an exception.
     */
    @Override
    public Token recoverInline(Parser recognizer)
            throws RecognitionException
    {
        System.err.println(" Error on line: " + recognizer.getCurrentToken().getLine());
        throw new RuntimeException(new InputMismatchException(recognizer));
    }

    /** Make sure we don't attempt to recover from problems in subrules. */
    @Override
    public void sync(Parser recognizer) { }
}
