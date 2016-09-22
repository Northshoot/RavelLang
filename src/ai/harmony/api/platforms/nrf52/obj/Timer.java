package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.platforms.RavelObject;
import ai.harmony.api.platforms.RavelObjectInterface;
import ai.harmony.ravel.primitives.Controller;
import org.stringtemplate.v4.ST;

/**
 * Created by lauril on 9/21/16.
 * This class deals simply with one timer creating naming and calling conventions
 *
 * Timmer queu is used to genrate timer_api file
 */
public class Timer extends RavelObject implements RavelObjectInterface {
    String mTimerMode;
    String mCallBack;
    String mDefName;
    String name ;
    Controller controller;

    public Timer(Controller ctr, String name){
        super();
        controller = ctr;
        name = name;
        docs = "This is timer documentation";
        mReturnType = "int32_t";
        mMethodName = "";
        mInitMethodName = "timers_init();";
        mTimerMode = "APP_TIMER_MODE_REPEATED";
        #define RANDOM_MODEL_CNTR_PERIODIC_TIME APP_TIMER_TICKS(1000, APP_TIMER_PRESCALER)
#define RANDOM_MODEL_CNTR_SINGLE_TIME APP_TIMER_TICKS(25000, APP_TIMER_PRESCALER)
        APP_TIMER_DEF(random_model__ctr_timer_periodic);
        APP_TIMER_DEF(random_model__ctr_timer_single);

        void timers_init(void);


/**
 * start timer
 */

        void random_ctr__startTimerPeriodic(uint32_t period);
        void random_ctr__stopTimerPeriodic();

        void startTimerOneShoot(uint64_t interval, app_timer_id_t timer);

        void stopTimer(app_timer_id_t timer);


        mCallBack = name + "__ctr_timer_periodic__expired";
        mDefName = name + "__ctr_timer_periodic";
        addToInclues("app_timer.h");
        addToMakeIncludePath("/components/drivers_nrf/timer");
        addToMakeIncludePath("/components/libraries/timer");
        addToMakeObj("/components/libraries/timer/app_timer.c");
    }

    public String getPeriodicStopCall(){
        return __stopTimerPeriodic();
    }
    public String getStartPeriodicCall(String period){
        timer_start_periodic.add("param", period);
        timer_start_periodic.add("name", controller.getName()) ;
        return timer_start_periodic.render();
    }
    @Override
    public String getImplementation() {
        timer_start_periodic.add("name", controller.getName()) ;
        return timer_start_periodic.render();

    }


}
