package ai.harmony.ravel.primitives;

import ai.harmony.ravel.translators.xLingual;

/**
 * Created by lauril on 7/21/16.
 */
public abstract class Primitive extends xLingual {
    public String mName;

    public Primitive(String name){
        super(name);
        this.mName = name;
    }


}
