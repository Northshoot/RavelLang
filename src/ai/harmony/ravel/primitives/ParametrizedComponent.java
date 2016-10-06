package ai.harmony.ravel.primitives;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 10/6/16.
 */
public class ParametrizedComponent extends Primitive{
    private static Logger LOGGER = Logger.getLogger(ParametrizedComponent.class.getName());

    protected Map<String,String> mParameterMap = new LinkedHashMap<>();
    public ParametrizedComponent(String name) {
        super(name);
    }


    public void addParam(String text) {
        mParameterMap.put(text, null);
    }

    public void setParam(String key, String value){
        if(mParameterMap.containsKey(key)){
            mParameterMap.put(key, value);
        } else {
            LOGGER.severe("No such paramer in the mParameterMap");
            throw new NoSuchElementException("Parameter with the name: " + key +
                    " does not exist. Available options: " + mParameterMap.keySet());

        }
    }

    public boolean hasParam(String key){
        return mParameterMap.containsKey(key);
    }

    public Set getParamsNames(){
        return mParameterMap.keySet();
    }
}
