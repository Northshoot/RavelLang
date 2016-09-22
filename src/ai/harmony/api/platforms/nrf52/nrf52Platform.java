package ai.harmony.api.platforms.nrf52;

import ai.harmony.api.platforms.nrf52.obj.Boot;
import ai.harmony.api.platforms.nrf52.obj.Random;
import ai.harmony.api.platforms.nrf52.obj.Timer;
import ai.harmony.api.platforms.nrf52.obj.TimerQueue;
import ai.harmony.ravel.primitives.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lauril on 9/21/16.
 */
public class nrf52Platform {
    private TimerQueue tQueue;
    private Random mRandom;
    private Boot mBoot;
    private Controller ctr;
    Map<String, Object> mFiles;
    public final static String BASE_PALTFORM_TMPL_PATH = "src/ai/harmony/api/platforms/nrf52/tmpl";
    public nrf52Platform (Controller ctr){
        this.ctr = ctr;
        mFiles = new HashMap<>();
        this.mRandom  = new Random();

    }

    public void addBoot(String name){
        if(mBoot == null) {
            this.mBoot = new Boot();
        }
        mFiles.put(name, this.mBoot);
    }
    public void addTimer(String timer_name){
        if(tQueue == null) {
            this.tQueue = new TimerQueue();
            mFiles.put("timers", this.tQueue);
        }
        tQueue.addTime(timer_name);
    }

    public Timer getTimer(String timer_name){
        return tQueue.getTimer(timer_name);
    }
    public Boot getBoot(){ return mBoot; }

    public Random getRandom() { return mRandom; }

    public Map<String, Object> getFiles(){
        return mFiles;

    }

}
