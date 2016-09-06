package ai.harmony.ravel.primitives;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by lauril on 8/16/16.
 */
public class ModelType {

    public static final int LOCAL = 1;
    public static final int STREAMING = 1;
    public static final int REPLICATED = 2;
    int mType;
    String mTypeString;
    private String _type;

    public ModelType(String type){
        _type =type;
        mTypeString = WordUtils.uncapitalize(_type);
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
