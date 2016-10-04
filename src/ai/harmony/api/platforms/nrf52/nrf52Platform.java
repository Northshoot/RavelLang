package ai.harmony.api.platforms.nrf52;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.platforms.nrf52.obj.Boot;
import ai.harmony.api.platforms.nrf52.obj.Random;
import ai.harmony.api.platforms.nrf52.obj.Timer;
import ai.harmony.api.platforms.nrf52.obj.TimerQueue;
import ai.harmony.ravel.primitives.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 9/21/16.
 */
public class nrf52Platform {
    private String mBuildPath;
    private TimerQueue tQueue;
    private Random mRandom;
    private Boot mBoot;
    private Controller ctr;
    List<FileObject> mFiles;
    public final static String BASE_PALTFORM_TMPL_PATH = "src/ai/harmony/api/platforms/nrf52/tmpl";
    public nrf52Platform (Controller ctr, String path){
        this.ctr = ctr;
        mFiles = new ArrayList<>();
        this.mRandom  = new Random();
        mBuildPath = path+"api/";
    }

    public void addBoot(String name){
        if(mBoot == null) {
            this.mBoot = new Boot(mBuildPath);
        }
        mFiles.addAll(mBoot.getFiles());
    }
    public void addTimer(String timer_name){
        if(tQueue == null) {
            this.tQueue = new TimerQueue(mBuildPath);

        }
        tQueue.addTimer(timer_name, true);
    }

    public Timer getTimer(String timer_name){
        return tQueue.getTimer(timer_name);
    }
    public Boot getBoot(){ return mBoot; }

    public Random getRandom() { return mRandom; }

    public List<FileObject> getFiles(){
        mFiles.addAll(this.tQueue.getFiles());
        return mFiles;

    }

}
