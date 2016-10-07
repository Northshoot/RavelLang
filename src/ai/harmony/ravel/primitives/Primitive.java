package ai.harmony.ravel.primitives;

import ai.harmony.ravel.translators.xLingual;

/**
 * Created by lauril on 7/21/16.
 */
public abstract class Primitive extends xLingual {
    public String mName;
    public String mInitName;

    public Primitive(String name){
        super(name);
        this.mName = name;
    }

    public String getInitMethodCall(){
        return "_innit();";
    }
    public String getInitMethodCallSize(){
        return mName.toLowerCase()+"_model__queue_innit_sized(int size);";
    }

    public String getDestroyMethodCall(){
        return mName.toLowerCase()+"_model__destroy_queue();";
    }
}
