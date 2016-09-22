package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.lang.c.FuncDeclaration;
import ai.harmony.api.platforms.RavelObject;
import ai.harmony.api.platforms.RavelObjectInterface;
import ai.harmony.ravel.primitives.Controller;
import org.stringtemplate.v4.ST;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lauril on 9/21/16.
 * This class deals simply with one timer creating naming and calling conventions
 *
 * Timmer queu is used to generate timer_api file
 */
public class Timer extends RavelObject implements RavelObjectInterface {
    String mTimerMode;
    String mCallBack;
    String name ;
    Controller controller;
    Map<String, FuncDeclaration> mFunctions;
    TimerQueue tm;
    FuncDeclaration start;
    FuncDeclaration stop;
    public boolean debug = true;
    private boolean periodic = true;

    public Timer( String name, TimerQueue tm, boolean periodic){
        super();
        this.tm = tm;
        this.periodic = periodic;
        name = name;
        docs = "This is timer " + name + " documentation";
        mTimerMode = "APP_TIMER_MODE_REPEATED";
        mFunctions = new HashMap<>();
        // implementation of timer functions
        //create function object
         start = new FuncDeclaration();
         stop = new FuncDeclaration();

        //add include to parent

        //add defines to parent
        //create default period
        if(periodic ) {
            tm.addToDefines(new Declaration(name.toUpperCase()+"__PERIODIC_TIME APP_TIMER_TICKS(1000, APP_TIMER_PRESCALER)",
                    "Default value for the timer" ));
            this.name = name+"__timer_periodic";
            //set function name
            start.setName(name+"__startTimerPeriodic");
            ST decl = tm.getTemplate("timer_start_periodic_declaration");
            decl.add("timer", this);
            decl.add("comment", start.getName() + " declaration");
            start.setMethodDeclaration(decl.render());

            ST impl = tm.getTemplate("timer_start_periodic_implementation");
            impl.add("timer", this);
            start.setFunctionImplementation(impl.render());

        } else {
            tm.addToDefines(new Declaration(name.toUpperCase()+"__SINGLE_TIME APP_TIMER_TICKS(25000, APP_TIMER_PRESCALER)",
                    "Default value for the timer" ));
            this.name = name+"__timer_single";
            //set function name
            start.setName(name+"__startTimerOneShoot");


        }

        tm.addToDefines(new Declaration("APP_TIMER_DEF(" + this.name +")", "Initializing timer"));
        //timers have two functions, start and stop,
        //add functions
        if (periodic ) {
            start.setMethodDeclaration("");
        } else {

        }
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


}
