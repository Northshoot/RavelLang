package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.lang.c.FuncDeclaration;
import ai.harmony.api.platforms.RavelAPIObject;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lauril on 9/21/16.
 * This class deals simply with one timer creating naming and calling conventions
 *
 * Timmer queu is used to generate timer_api file
 */
public class Timer extends RavelAPIObject {
    String mTimerMode;
    String mCallBack;
    String mVarName ;
    String mShortName;
    String mTemplateName;
    Map<String, FuncDeclaration> mFunctions;
    TimerQueue tm;
    FuncDeclaration start;
    FuncDeclaration stop;
    public boolean debug = true;
    private boolean periodic = true;

    public Timer( String n, TimerQueue tm, boolean periodic){
        super();
        this.tm = tm;
        this.periodic = periodic;
        this.mShortName = n;
        this.mVarName = n;
        docs = "This is timer " + this.mVarName + " documentation";
        mTimerMode = "APP_TIMER_MODE_REPEATED";
        mFunctions = new HashMap<>();
        // implementation of timer functions
        //timers have two functions, start and stop,
         start = new FuncDeclaration();
         stop = new FuncDeclaration();
        //fixing naming depending on the periodic or not
        if(periodic ) {
            this.mVarName = mShortName+"__timer_periodic";
            this.mTemplateName = "__timer_periodic";
            tm.addToDefines(new Declaration(this.mVarName.toUpperCase() + " APP_TIMER_TICKS(1000, APP_TIMER_PRESCALER)",
                    "Default value for the timer" ));
            //set function name
            start.setName(mShortName+"__startTimerPeriodic");
            stop.setName(mShortName+"__stopTimerPeriodic");
        } else {
            this.mVarName = mShortName+"__timer_periodic";
            tm.addToDefines(new Declaration(this.mVarName +" APP_TIMER_TICKS(25000, APP_TIMER_PRESCALER)",
                    "Default value for the timer" ));
            //set function name
            start.setName(mShortName+"__startTimerOneShoot");
            stop.setName(mShortName+"__stopTimerOneShoot");
        }
        //creating functions and all other stuff
        mCallBack = this.mVarName+"__expired";

        ST start_decl = getTemplate("start", "__declaration");
        start_decl.add("timer", start);
        start.setMethodDeclaration(start_decl.render());

        ST start_impl = getTemplate("start" , "__implementation");
        start_impl.add("timer", start);
        start.setFunctionImplementation(start_impl.render());

        ST stop_decl = getTemplateStop("__declaration");
        stop_decl.add("timer", stop);
        stop.setMethodDeclaration(stop_decl.render());

        ST stop_impl = getTemplateStop("__implementation");
        stop_impl.add("timer", stop);
        stop.setFunctionImplementation(stop_impl.render());

        //add functions
        tm.addToDefines(new Declaration("APP_TIMER_DEF(" + this.mVarName +")", "Initializing timer"));
        tm.addFuncDeclaration(start);
        tm.addFuncDeclaration(stop);
    }

    public String getStopCall(){
        if (periodic ) {
            return start.getCallFunction();
        }
        return "";
    }
    public String getStartPeriodicCall(String period){
        if (periodic ) {
            return start.getCallFunction();
        }
        return "";
    }

    public String getTimerMode(){
        return this.mTimerMode;
    }

    public  String getCallBack() { return this.mCallBack; }

    private ST getTemplateStop(String post){
        return tm.getTemplate("stop__timer"+post);
    }
    private ST getTemplate(String pre, String post){
        return tm.getTemplate(pre + this.mTemplateName + post);
    }
}
