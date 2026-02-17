package ai.harmony.ravel.api;

/**
 * Created by gcampagn on 1/29/17.
 */
public class InvalidOptionException extends Exception {
    public InvalidOptionException(String message) {
        super(message);
    }
    public InvalidOptionException(String message, Exception cause) {
        super(message, cause);
    }
}
