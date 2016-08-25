package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;

/**
 * Created by lauril on 7/29/16.
 */
public class Space extends Primitive{
    String mTransmitFunction;
    public Space(String name){
        super(name);
    }

    public String getTransmitFunction(){
        return "random_char_update";
    }

    public String getService(){
        return "ravel_service";
    }
}
