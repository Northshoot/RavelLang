package patterns.src.java.sources;

import patterns.src.java.controller.ModelController;
import org.stanford.ravel.rrt.TimerSource;

/**
 * Created by lauril on 1/30/17.
 */
public class TimerSource1 extends TimerSource {
    /**
     * Controller Listeners
     */
    //AUTOGEN: controller naming
    private ModelController mcntr;
    public void setModelController(ModelController mc){
        this.mcntr = mc;
    }

    @Override
    protected void dispatch() {
        mcntr.timer_1_fired();
    }
}
