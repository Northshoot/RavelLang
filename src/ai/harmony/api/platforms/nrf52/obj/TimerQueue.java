package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.platforms.RavelObject;
import ai.harmony.api.platforms.RavelObjectInterface;
import ai.harmony.ravel.primitives.Controller;
import org.apache.commons.lang3.text.WordUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static ai.harmony.api.lang.c.CLang.BASE_C_LANG_TMPL_PATH;
import static ai.harmony.api.platforms.nrf52.nrf52Platform.BASE_PALTFORM_TMPL_PATH;

/**
 * Created by lauril on 9/21/16.
 */
public class TimerQueue extends RavelObject implements RavelObjectInterface {

    STGroup tmpl_group;
    STGroup tmpl_header;
    STGroup tmpl_obj;
    private Map<String, Timer> mTimerMap;

    public Controller controller;
    public String fileName = "api_timer";
    public String innit_name = "innit_timer";

    public TimerQueue( ){
        super();
        mTimerMap = new HashMap<>();
        tmpl_group = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/timer.stg");
        tmpl_header = new STGroupFile(BASE_C_LANG_TMPL_PATH + "/h_file.stg");
        docs = "This is timer documentation";
        mReturnType = "int32_t";
        mMethodName = "";
        mInitMethodName = "timers_init";

        addToInclues(new Declaration("<stdint.h>", "Used for uint type"));
        addToInclues(new Declaration("\"app_error.h\"", "Used to deterime error"));
        addToInclues(new Declaration("\"softdevice_handler.h\""));
        addToInclues(new Declaration("\"app_timer.h\""));
        addToMakeIncludePath(new Declaration("/components/drivers_nrf/timer"));
        addToMakeIncludePath(new Declaration("/components/libraries/timer"));
        addToMakeObj(new Declaration("/components/libraries/timer/app_timer.c"));
    }

    public String CallInnit(){
        return mInitMethodName + "();";
    }

    public String getHeaderFileName(){
        return fileName + ".h";
    }

    @Override
    public String getHeaderDefName(){
        return fileName.toUpperCase() + "_H";
    }
    @Override
    public String getImplementation() {
        return null;
    }


    @Override
    public String getInitImplementation() {
        String implementation = "";
        return implementation;
    }

    public void addTime(String timer_name) {
        mTimerMap.put(timer_name, new Timer(timer_name));
    }

    public Timer getTimer(String timer_name) {
        return mTimerMap.get(timer_name);
    }

    @Override
    public String toString(){
        ST r = tmpl_header.getInstanceOf("header");
        r.add("header_data", this);
        return r.render();
    }
}
