package patterns.src.java.rrt;

import patterns.src.java.controller.ModelController;

import java.util.Timer;

/**
 * Created by lauril on 1/26/17.
 */
public class RavelTimer  {

    //AUTOGEN: number of timers that are used
    Timer timer_cntr_1;

    //AUTOGEN: controllers that use timer
    ModelController mModelCTR;

    public RavelTimer(ModelController mctrh){
        //AUTOGEN: init and assign ( we need to pass all model controllers
        this.mModelCTR = mctrh;
        timer_cntr_1 = new Timer();
    }


}
