package ai.harmony.ravel.api.lang;

/**
 * Converts a literal to a String.
 * This is a delegate interface used by STIRTranslator.
 *
 * Created by gcampagn on 1/29/17.
 */
@FunctionalInterface
public interface LiteralFormatter {
    String toLiteral(Object o);
}
