package ai.harmony.ravel.primitives;
import ai.harmony.ravel.primitives.Fields.Field;
import org.stringtemplate.v4.*;

/**
 * Created by lauril on 8/16/16.
 */
public class Source extends Primitive{

    private String identifier;
    private String resolve;

    public Source(String name, String res) {
        super(name);
        identifier = name;
        resolve = res;
    }



        public void source(String name, String resolve){
            this.identifier = name;
            this.resolve = resolve;
        }

        public String getSinkIdentifier() {return this.identifier; }
        public String getSinkReference() {return this.resolve; }
}
