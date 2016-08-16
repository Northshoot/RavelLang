package ai.harmony.ravel.primitives;
import org.stringtemplate.v4.*;

/**
 * Created by lauril on 8/16/16.
 */
public class Source {
    private Field mField;
    private ST st;

    public Source(Field field, ST st){
        this.mField = field;
        this.st = st;
    }
}
