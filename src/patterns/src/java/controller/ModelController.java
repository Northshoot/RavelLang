package patterns.src.java.controller;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.TimerSource;
import patterns.src.java.model.Model;
import patterns.src.java.sources.TimerSource1;


/**
 * Created by lauril on 1/23/17.
 */
public class ModelController {

    protected String mName ;
    //AUTOGEN: models that controller is using
    private Model mModel;

    //AUTOGEN: sources
    private TimerSource timer;

    //AUTOGEN: sinks

    //AUTOGEN: global vars
    private boolean running;

    public boolean start = false;
    public ModelController(Model model, TimerSource timersource){

        //AUTOGEN: bind models
        this.mModel = model;

        //AUTOGEN:create timers
        // initialize timer from AppDispatcher
        this.timer = timersource;

        //We set DThread to true.
        //A daemon thread will execute only as long as the rest of the program continues to execute.
    }

    void pprint(String s){
        System.out.println("[" + this.mName +"]>" + s);
    }
    public void setName(String name){
        this.mName = name;
    }

    public void timer_1_fired() {
        //create a record and save it.
        Model.Record rec = mModel.create();
        rec.field1 = 1444444;
        rec.field2 = rec.field1 + 2;
        rec.field3 = rec.field2 * 3;
        rec.field4 = rec.field3 / 2;
        Context ctx = mModel.save(rec);

        pprint("model_save(): " + ctx.error);
    }

    public void Model_arrived(Context<Model.Record> ctx){
        //TODO: add to the queue
        pprint("RX " + ctx);
    }

    public void Model_departed(Context<Model.Record> ctx){
        if(!running) timer.start_periodic(3000);
        pprint("Model departed: " + ctx);

    }

    public void Model_full(Context<Model.Record> ctx){
        if(mName == "EMD" ) {
            timer.cancel();
            running = false;
        }

        pprint("Model_full: " + ctx);

    }

    public void Model_save_done(Context<Model.Record> ctx){
        pprint("Model_save_done:" + ctx);
    }

    /**
     * AUTOGEN methods that controller subscribes in Ravel
     */
    public void system_started() {
        //TODO: test only in simulation
        if(mName == "EMD" ) {
            timer.start_periodic(3000);
            running = true;
        }

        pprint("system started: " + start);
    }

    public String getName() {
        return mName;
    }
}
