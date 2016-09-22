package ai.harmony.api.platforms.nrf52;

import ai.harmony.api.platforms.nrf52.obj.Boot;
import ai.harmony.api.platforms.nrf52.obj.Random;
import ai.harmony.api.platforms.nrf52.obj.Timer;
import ai.harmony.ravel.primitives.Controller;

import java.util.List;
import java.util.Map;

/**
 * Created by lauril on 9/21/16.
 */
public class nrf52Platform {
    private Map<String, Timer> mTimer;
    private Random mRandom;
    private Boot mBoot;
    private Controller ctr;

    public nrf52Platform (Controller ctr){
        this.ctr = ctr;
        this.mBoot = new Boot();
        this.mRandom  = new Random();

    }

    public void addTimer(String timer_name){
        this.mTimer.put(timer_name, new Timer(ctr, timer_name));
    }

    public Timer getTimer(String timer_name){
        return mTimer.get(timer_name);
    }
    public Boot getBoot(){ return mBoot; }

    public Random getRandom() { return mRandom; }


}
