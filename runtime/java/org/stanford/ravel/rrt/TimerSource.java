package org.stanford.ravel.rrt;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lauril on 1/30/17.
 */
public abstract class TimerSource {
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            dispatch();
        }
    };
    private Timer timer = new Timer();

    public void start_one_shot(int time) {
        timer.schedule(task, time);
    }
    public void cancel() {
        timer.cancel();
    }
    public void start_periodic(int time) {
        timer.scheduleAtFixedRate(task, 0, time);
    }

    protected abstract void dispatch();
}
