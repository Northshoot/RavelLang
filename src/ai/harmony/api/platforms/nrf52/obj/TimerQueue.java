package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.lang.c.FuncDeclaration;
import ai.harmony.api.platforms.RavelAPIObject;
import ai.harmony.api.platforms.RavelObjectInterface;
import ai.harmony.ravel.primitives.Controller;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ai.harmony.api.lang.c.CLang.BASE_C_LANG_TMPL_PATH;
import static ai.harmony.api.platforms.nrf52Platform.BASE_PALTFORM_TMPL_PATH;

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


    public TimerQueue(String mBuildPath){
        super();
        mTimerMap = new HashMap<>();
        tmpl_group = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/timer.stg");
        tmpl_header = new STGroupFile(BASE_C_LANG_TMPL_PATH + "/h_file.stg");
        tmpl_obj = new STGroupFile(BASE_C_LANG_TMPL_PATH + "/c_obj.stg");
        docs = "This is timer file documentation documentation" +
                "All timers are collected in the single file";
        mTimerMap = new HashMap<>();

        mBuildPath = mBuildPath;
        header.setFileName(fileName + ".h");
        header.setPath(mBuildPath);
        obj.setFileName(fileName + ".c");
        obj.setPath(mBuildPath);


        //to all timers needed includes
        addToIncludes(new Declaration("<stdint.h>", "Used for uint type"));
        addToIncludes(new Declaration("\"app_error.h\"", "Used to deterime error"));
        addToIncludes(new Declaration("\"softdevice_handler.h\""));
        addToIncludes(new Declaration("\"app_timer.h\""));

        addToMakeIncludePathSDK("/components/drivers_nrf/timer");
        addToMakeIncludePathSDK("/components/libraries/timer");
        addToMakeObjSDK("/components/libraries/timer/app_timer.c");
        //TODO: all these should be aggregated sorted from builder class
        addToMakeObj("api/" + this.fileName +".c");
    }

    private void make_init() {
        FuncDeclaration f = new FuncDeclaration();
        f.setCallFunction(innit_name +"();");
        f.setMethodDeclaration("void " + f.getCallFunction());
        ST tmpl = tmpl_group.getInstanceOf(innit_name);
        //show the map of timers and puf!
        tmpl.add("timers", this);
        f.setFunctionImplementation( tmpl.render() );
        addFuncDeclaration(f);
    }

    public ST getTemplate(String name){
        return tmpl_group.getInstanceOf(name);
    }

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


    public void addTimer(String timer_name, boolean periodic) {
        mTimerMap.put(timer_name, new Timer(timer_name, this, periodic));
    }

    public Timer getTimer(String timer_name) {
        return mTimerMap.get(timer_name);
    }

    public List<FileObject> getFiles() {
        make_init();
        ST r = tmpl_header.getInstanceOf("header");
        r.add("header_data", this);
        header.setContent(r.render());
        ST t_obj = tmpl_obj.getInstanceOf("obj_file");

        t_obj.add("obj_data", this);
        obj.setContent(t_obj.render());

        return super.getFiles();
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
