package ai.harmony.api.platforms.nrf52.obj;

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
    String mDefName;
    String name ;
    String mPostfix =
    Controller controller;
    Map<String, FuncDeclaration> mFunctions;
    TimerQueue tm;

    public Timer( String name, TimerQueue tm){
        super();
        this.tm = tm;
        name = name;
        docs = "This is timer " + name + " documentation";
        mTimerMode = "APP_TIMER_MODE_REPEATED";
        mFunctions = new HashMap<>();
        // implementation of timer functions
        //create function object
        FuncDeclaration mFunction = new FuncDeclaration();
        //set function name
        mFunction.setName(name);
        // add call name

        //add return type

        //add include to parent

        //add defines to parent
        //create default period
        tm.

        //add declaration

        //add implementation


        mFunction.
//        #define RANDOM_MODEL_CNTR_PERIODIC_TIME APP_TIMER_TICKS(1000, APP_TIMER_PRESCALER)
//#define RANDOM_MODEL_CNTR_SINGLE_TIME APP_TIMER_TICKS(25000, APP_TIMER_PRESCALER)
//        APP_TIMER_DEF(random_model__ctr_timer_periodic);
//        APP_TIMER_DEF(random_model__ctr_timer_single);

//


/**
 * start timer
 */
//
//        void random_ctr__startTimerPeriodic(uint32_t period);
//        void random_ctr__stopTimerPeriodic();
//
//        void startTimerOneShoot(uint64_t interval, app_timer_id_t timer);
//
//        void stopTimer(app_timer_id_t timer);


        mCallBack = name + "__ctr_timer_periodic__expired";
        mDefName = name + "__ctr_timer_periodic";

    }

    public String getPeriodicStopCall(){
        return "";
    }
    public String getStartPeriodicCall(String period){
        return "";
    }
    @Override
    public String getImplementation() {
        return "";

    }

    @Override
    public String getHeaderDefName() {
        return null;
    }


}
