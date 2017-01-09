package org.stanford.api.platforms.nrf52.obj;

import org.stanford.api.platforms.RavelAPIObject;
import org.stanford.ravel.primitives.Source;

/**
 * Created by lauril on 9/21/16.
 * This class deals simply with one timer creating naming and calling conventions
 *
 * Timer queue is used to generate timer_api file
 * TODO: optimize the implementation too much writing for a single timer
 *
 */
public class Timer extends RavelAPIObject {
    String mTimerMode;
    String mCallBack;
    String mVarName ;
    String mShortName;
    String mStartName;
    String mStopName;
    TimerQueue tm;
    Source mSrc;

    public boolean debug = true;
    private boolean periodic = true;

    public Timer(Source src, TimerQueue tm, boolean periodic){
        super();
        mSrc =src;
        this.tm = tm;
        this.periodic = periodic;
        this.mShortName = src.getCName();
        this.mVarName = src.getCName();

        docs = "This is timer " + this.mVarName + " documentation";

        // implementation of timer functions
        if(periodic ) {
            mTimerMode="APP_TIMER_MODE_REPEATED";

            this.mVarName = mShortName+"__timer_periodic";
        } else {
            mTimerMode="APP_TIMER_MODE_SINGLE_SHOT";
            this.mVarName = mShortName+"__timer_one_shot";
        }
        //creating functions and all other stuff
        mCallBack = this.mVarName+"__expired";
        src.addCallBack(mCallBack);
        mStartName = this.mVarName + "__start";
        mStopName = this.mVarName + "__stop";

    }


    public boolean isPeriodic(){ return periodic; }
    public String getCallback(){ return this.mCallBack;}

    public String getStartName(){
        return  mStartName;
    }
    public String getStopName(){
        return  mStopName;
    }
    public String getCVarName(){
        return "m_" + this.mVarName;
    }

    public String getDefineName(){ return this.mVarName.toUpperCase();}
    public String getTimerMode(){
        return this.mTimerMode;
    }

    public  String getCallBack() { return this.mCallBack; }

}
