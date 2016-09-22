package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.platforms.RavelObject;
import ai.harmony.api.platforms.RavelObjectInterface;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lauril on 9/21/16.
 * TODO: for now we assume that this API is only for C and we never try to build it in java
 *
 */
public class Temperature extends RavelObject implements RavelObjectInterface {




    public Temperature() {
        super();
        docs = "This is Temperature documentation";
        mReturnType = "int32_t";
        mMethodName = "getTemperature()";
        addToInclues(new Declaration("softdevice_handler.h"));
        addToMakeIncludePath(new Declaration("/components/softdevice/common/softdevice_handler"));
        addToMakeObj(new Declaration("/components/softdevice/common/softdevice_handler/softdevice_handler.c"));
    }

    //TODO: allow configuration with the variable naming
    @Override
    public String getImplementation(){
        String implementation;
        implementation = "" +
                "int32_t getTemperature(){" +
                "   int32_t temperature;" +
                "   sd_temp_get(&temperature);" +
                "   return temperature;" +
                "}";
        return implementation;
    }

    @Override
    public String getHeaderDefName() {
        return null;
    }

    @Override
    public String getReadMethod() {
        return mMethodName;
    }


}
