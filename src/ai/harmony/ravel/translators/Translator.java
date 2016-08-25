package ai.harmony.ravel.translators;

/**
 * Created by lauril on 8/23/16.
 */
public interface Translator {

    /**
     * Returns verbose name of the component or primitive
     * @return String verbose name
     */
    String getVerboseName();

    /**
     * Returns C style name of the component or primitive
     * @return String C style
     */
    String getCStructName();
    String getCVarName();

    /**
     * Returns Java style name of the component or primitive
     * @return String name Java style
     */
    String getJavaClassName();
    String getJavaVarName();

    /**
     * Returns Python style of the component or primitive
     * @return String verbose name
     */
    String getPythonClassName();
    String getPythonVarName();

}
