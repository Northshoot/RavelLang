package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.lang.c.FuncDeclaration;
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
 * This file is example of the platform specific API for code deneration
 * It allwos dynamic creation of timers so you can add "unlimited" number of them
 *
 *
 */
public class TimerQueue extends RavelObject implements RavelObjectInterface {

    STGroup tmpl_group;
    STGroup tmpl_header;
    STGroup tmpl_obj;
    private Map<String, Timer> mTimerMap;

    public Controller controller;
    public String fileName = "api_timer";
    public String innit_name = "timers_init";


    public TimerQueue( ){
        super();
        mTimerMap = new HashMap<>();
        tmpl_group = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/timer.stg");
        tmpl_header = new STGroupFile(BASE_C_LANG_TMPL_PATH + "/h_file.stg");
        docs = "This is timer file documentation documentation" +
                "All timers are collected in the single file";
        mTimerMap = new HashMap<>();

        //to all timers needed includes
        addToInclues(new Declaration("<stdint.h>", "Used for uint type"));
        addToInclues(new Declaration("\"app_error.h\"", "Used to deterime error"));
        addToInclues(new Declaration("\"softdevice_handler.h\""));
        addToInclues(new Declaration("\"app_timer.h\""));
        addToMakeIncludePath(new Declaration("/components/drivers_nrf/timer"));
        addToMakeIncludePath(new Declaration("/components/libraries/timer"));
        addToMakeObj(new Declaration("/components/libraries/timer/app_timer.c"));
        addToMakeObj(new Declaration("/api/" + this.fileName +".c"));
    }

    private void make_init() {
        FuncDeclaration f = new FuncDeclaration();
        f.setCallFunction(innit_name +"();");
        f.setMethodDeclaration("void " + f.getCallFunction());
        ST tmpl = tmpl_group.getInstanceOf("timers_init");
        //show the map of timers and puf!
        tmpl.add("timers", mTimerMap);
        f.setFunctionImplementation( tmpl.render() );
        addFuncDeclaration(f);
    }

    public ST getTemplate(String name){
        return tmpl_group.getInstanceOf(name);
    }


    public String getHeaderFileName(){
        return fileName + ".h";
    }


    @Override
    public String getHeaderDefName(){
        return fileName.toUpperCase() + "_H";
    }


    public void addTime(String timer_name, boolean periodic) {
        mTimerMap.put(timer_name, new Timer(timer_name, this, periodic));
    }

    public Timer getTimer(String timer_name) {
        return mTimerMap.get(timer_name);
    }

    @Override
    public String toString(){
        //create the init method
        make_init();

        ST r = tmpl_header.getInstanceOf("header");
        r.add("header_data", this);
        return r.render();
    }
}
