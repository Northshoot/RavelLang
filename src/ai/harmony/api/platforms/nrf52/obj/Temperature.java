package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.platforms.RavelAPIObject;
import ai.harmony.api.platforms.RavelObjectInterface;

/**
 * Created by lauril on 9/21/16.
 * TODO: for now we assume that this API is only for C and we never try to build it in java
 *
 */
public class Temperature extends RavelAPIObject implements RavelObjectInterface {




    public Temperature() {
        super();
        docs = "This is Temperature documentation";
        mMethodName = "getTemperature()";
        addToInclues(new Declaration("softdevice_handler.h"));
        addToMakeIncludePath(new Declaration("/components/softdevice/common/softdevice_handler"));
        addToMakeObj(new Declaration("/components/softdevice/common/softdevice_handler/softdevice_handler.c"));
    }

    //TODO: allow configuration with the variable naming

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
