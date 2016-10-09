package ai.harmony.ravel.primitives;
import ai.harmony.ravel.primitives.Fields.Field;
import org.stringtemplate.v4.*;

/**
 * Created by lauril on 8/16/16.
 */
public class Source extends Primitive{

    private String identifier;
    private String resolve;
    private String mInitCallName = null;

    public Source(String name, String res) {
        super(name);
        identifier = name;
        resolve = res;
    }



        public void source(String name, String resolve){
            this.identifier = name;
            this.resolve = resolve;
        }

    public boolean getInit(){
        if(mInitCallName == null ){
            return false;
        } else {
            return true;
        }
    }
     public void setInitCallName(String s){
         mInitCallName = s;
     }


    public String getInitMethodCall(){
        return mInitCallName;
    }
        public String getSinkIdentifier() {return this.identifier; }
        public String getSinkReference() {return this.resolve; }
}
