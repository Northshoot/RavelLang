package ai.harmony.ravel.error;


/**
 * Created by lauril on 8/25/16.
 */
public class NotImplementedException extends Exception {
    public NotImplementedException(String m) { super("Method " + m + " not implemented!"); }

}