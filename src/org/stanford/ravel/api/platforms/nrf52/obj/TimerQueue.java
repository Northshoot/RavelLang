package org.stanford.ravel.api.platforms.nrf52.obj;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.c.FuncDeclaration;
import org.stanford.ravel.api.platforms.RavelAPIObject;
import org.stanford.ravel.api.platforms.RavelObjectInterface;
import org.stanford.ravel.primitives.Controller;
import org.stanford.ravel.primitives.Source;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.stanford.ravel.api.platforms.nrf52Platform.BASE_PALTFORM_TMPL_PATH;

/**
 * Created by lauril on 9/21/16.
 * This file is example of the platform specific API for code deneration
 * It allwos dynamic creation of timers so you can add "unlimited" number of them
 *
 *
 */
public class TimerQueue extends RavelAPIObject implements RavelObjectInterface {

    STGroup tmpl_group;
    STGroup tmpl_header;
    STGroup tmpl_obj;
    private Map<String, Timer> mTimerMap;

    public Controller controller;
    private String fileName = "api_timer";
    public String innit_name = "timers_init";


    public TimerQueue(String mBuildPath, Controller c){
        super();
        controller = c;
        mTimerMap = new HashMap<>();
        tmpl_group = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/timer.stg");
        docs = "This is timer file documentation documentation" +
                "All timers are collected in the single file";

        header.setFileName(fileName + ".h");
        header.setPath(mBuildPath);
        obj.setFileName(fileName + ".c");
        obj.setPath(mBuildPath);

        addToMakeIncludePathSDK("/components/drivers_nrf/timer");
        addToMakeIncludePathSDK("/components/libraries/timer");
        addToMakeObjSDK("/components/libraries/timer/app_timer.c");
        //TODO: all these should be aggregated sorted from builder class
        addToMakeObj("api/" + this.fileName +".c");
    }

    public String getControllerInclude(){

        return controller.getHeaderFileName();}

    public List<FuncDeclaration> getFuncDeclaration(){

        return  super.getFuncDeclaration();
    }
    public String getHeaderFileName(){
        return fileName + ".h";
    }

    public List<Timer> getTimers(){
        List<Timer> t = new ArrayList<>();
        t.addAll(mTimerMap.values());
        return t;
    }
    public String getInitMethodName(){
        return innit_name;
    }

    @Override
    public String getHeaderDefName(){
        return fileName.toUpperCase() + "_H";
    }


    public void addTimer(Source timer_name, boolean periodic) {
        mTimerMap.put(timer_name.getCName(), new Timer(timer_name, this, periodic));
    }

    public Timer getTimer(String timer_name) {
        return mTimerMap.get(timer_name);
    }

    public List<FileObject> getFiles() {
        ST r = tmpl_group.getInstanceOf("timers_header");
        r.add("obj", this);
        header.setContent(r.render());

        ST t_obj = tmpl_group.getInstanceOf("timer_object");
        t_obj.add("timers", this);
        t_obj.add("include", getControllerInclude());
        obj.setContent(t_obj.render());

        return super.getFiles();
    }
    @Override
    public String toString(){
        return "TQueus: " + mTimerMap.size();
    }
}
