package patterns.src.java.controller;

import patterns.src.java.model.Model;
import patterns.src.java.rrt.Context;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lauril on 1/23/17.
 */
public class ModelController extends ModelCtrl {

    //AUTOGEN: models that controller is using
    private Model mModel;

    //AUTOGEN: sources

    //AUTOGEN: sinks

    //AUTOGEN: systems components
    Timer controller_timer_1;

    //AUTOGEN: timer tasks
    ControllerNameTimerNameTask timerTask;



    public ModelController(Model model){
        //AUTOGEN: bind models
        this.mModel = model;
        //AUTOGEN: set controller name
        name = "ModelController";
        //AUTOGEN: set controller to the model for callbacks
        mModel.setController(this);

        //AUTOGEN:create timers
        //We set DThread to true.
        //A daemon thread will execute only as long as the rest of the program continues to execute.
        controller_timer_1 = new Timer("timer_name", false);
        timerTask = new ControllerNameTimerNameTask(this);
        controller_timer_1.scheduleAtFixedRate(timerTask,0,1000);

    }

    public void ControllerNameTimerNameTask_call_back(){
        System.out.println("Callback: tick" );
    }
    public void arrived(Context ctx){
        System.out.println(ctx);
    }

    public void departed(Context ctx){
        System.out.println(ctx);

    }

    public void full(Context ctx){
        System.out.println(ctx);

    }

    public void save_done(Context ctx){
        System.out.println(ctx);
    }

    /**
     * AUTOGEN methods that controller subscribes in Ravel
     */
    public void system_started() {
        System.out.println("System Started");
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
            System.out.println("Running timer task with counter: "+ counter++);
            mcrt.ControllerNameTimerNameTask_call_back();
        }

    }
}
