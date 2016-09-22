package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.platforms.RavelObject;
import ai.harmony.api.platforms.RavelObjectInterface;
import ai.harmony.ravel.primitives.Controller;
import org.apache.commons.lang3.text.WordUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * Created by lauril on 9/21/16.
 */
public class TimerQueue extends RavelObject implements RavelObjectInterface {

    STGroup tmpl_group;
    ST tmpl_header;
    ST tmpl_obj;

    public Controller controller;
    public String fileName = "api_timer";
    public String innit_name = "innit_timer";

    public TimerQueue(Controller ctr, String name){
        super();
        tmpl_group = new STGroupFile("tmpl/timer.stg");
        tmpl_header = new ST("../lang/c/tmpl/h_file.st");
        controller = ctr;
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
        return WordUtils.capitalize(fileName) + "_H";
    }
    @Override
    public String getImplementation() {
        return null;
    }


    @Override
    public String getInitImplementation() {
        String implementation = "" ;
        return implementation;
    }
}
