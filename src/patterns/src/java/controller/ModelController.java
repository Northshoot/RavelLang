package patterns.src.java.controller;

import org.stanford.ravel.rrt.Context;
import patterns.src.java.model.Model;
import patterns.src.java.sources.TimerSource1;

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
    private TimerSource1 timer;

    //AUTOGEN: sinks

    //AUTOGEN: global vars
    private boolean running;

    public boolean start = false;
    public ModelController(Model model){

        //AUTOGEN: bind models
        this.mModel = model;

        //AUTOGEN:create timers
        // initialize timer from AppDispatcher

        //We set DThread to true.
        //A daemon thread will execute only as long as the rest of the program continues to execute.
    }

    public void setName(String name){
        this.mName = name;
    }

    public void timer_1_fired() {
        //create a record and save it.
        Model.Record rec = mModel.create();
        rec.field1 = 1;
        rec.field2 = rec.field1 + 2;
        rec.field3 = rec.field2 * 3;
        rec.field4 = rec.field3 / 2;
        Context ctx = mModel.save(rec);

        System.out.println(ctx.error);
    }

    public void Model_arrived(Context<Model.Record> ctx){
        System.out.println(ctx);
    }

    public void Model_departed(Context<Model.Record> ctx){
        if(!running) timer.start_periodic(1000);
        System.out.println(ctx);

    }

    public void Model_full(Context<Model.Record> ctx){
        timer.cancel();
        running = false;
        System.out.println(ctx);

    }

    public void Model_save_done(Context<Model.Record> ctx){
        System.out.println(ctx);
    }

    /**
     * AUTOGEN methods that controller subscribes in Ravel
     */
    public void system_started() {
        timer.start_periodic(1000);
        running = true;
        //TODO: test only in simulation
        System.out.println("Controller {" + this.mName +"} started: " + start);
    }

    public String getName() {
        return mName;
    }
}
