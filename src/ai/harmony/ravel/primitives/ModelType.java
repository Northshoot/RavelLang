package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;

/**
 * Created by lauril on 8/16/16.
 */
public class ModelType {

    public static final int LOCAL = 1;
    public static final int STREAMING = 1;
    public static final int REPLICATED = 2;
    int mType;
    String mTypeString;

    public ModelType(RavelParser.ModelTypeContext ctx){
        mTypeString = ctx.getText().toString();
        switch (mTypeString){
            case "streaming" : {
                mType = ModelType.STREAMING;
                break;
            }case "local" : {
                mType = ModelType.LOCAL;
                break;
            }case "replicated" : {
                mType = ModelType.REPLICATED;
                break;
            }
        }
    }

    public int modelType(){
        return mType;
    }

    public String modelTypeString(){
        return mTypeString;
    }

    public String toString(){
        return mTypeString;
    }



}
