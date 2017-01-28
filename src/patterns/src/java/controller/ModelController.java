package patterns.src.java.controller;

import patterns.src.java.model.Model;
import patterns.src.java.rrt.Context;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lauril on 1/23/17.
 */
public class ModelController {

    protected String mName ;
    //AUTOGEN: models that controller is using
    private Model mModel;

    //AUTOGEN: sources

    //AUTOGEN: sinks

    //AUTOGEN: systems components
    Timer controller_timer_1;

    //AUTOGEN: timer tasks
    ControllerNameTimerNameTask timerTask;
    boolean controller_timer_1_running=false;

    public boolean start = false;
    public ModelController(Model model){

        //AUTOGEN: bind models
        this.mModel = model;
        //AUTOGEN: set controller to the model for callbacks
        mModel.setController(this);

        //AUTOGEN:create timers
        //We set DThread to true.
        //A daemon thread will execute only as long as the rest of the program continues to execute.
    }

    public void setName(String name){
        this.mName = name;
    }

    //TEMP: start the timer
    public void start_timer(){
        controller_timer_1_running = true;
        controller_timer_1.scheduleAtFixedRate(timerTask,0,1000);
    }

    public void ControllerNameTimerNameTask_call_back(){
        //create a record and save it.
        Model.Record rec = mModel.create();
        rec.field1 = 1;
        rec.field2 = rec.field1 + 2;
        rec.field3 = rec.field2 * 3;
        rec.field4 = rec.field3 / 2;
        Context ctx = mModel.save(rec);

        System.out.println(ctx.mError);
    }

    public void arrived(Context ctx){
        System.out.println(ctx);
    }

    public void departed(Context ctx){
        if(!controller_timer_1_running) start_timer();
        System.out.println(ctx);

    }

    public void full(Context ctx){
        controller_timer_1.cancel();
        controller_timer_1_running = false;
        System.out.println(ctx);

    }

    public void save_done(Context ctx){
        System.out.println(ctx);
    }

    /**
     * AUTOGEN methods that controller subscribes in Ravel
     */
    public void system_started() {
        controller_timer_1 = new Timer("timer_name_"+this.mName, false);
        timerTask = new ControllerNameTimerNameTask(this);
        //TODO: test only in simulation
        System.out.println("Controller {" + this.mName +"} started: " + start);
        if(start) {
            start_timer();
        }
    }

    public String getName() {
        return mName;
    }

    /**
     * all timer tasks are generated as inner controller classes extending TimerTask
     * TODO: evaluate performance
     */

    class ControllerNameTimerNameTask extends TimerTask{

        private int counter = 0;
        ModelController mcrt;

        public ControllerNameTimerNameTask(ModelController mcrt){
            //We set up controller for callbacks
            this.mcrt = mcrt;
        }
        public void run(){
            mcrt.ControllerNameTimerNameTask_call_back();
        }

    }
}
