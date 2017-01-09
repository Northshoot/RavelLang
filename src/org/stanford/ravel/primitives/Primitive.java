package org.stanford.ravel.primitives;

import org.stanford.ravel.translators.xLingual;

/**
 * Created by lauril on 7/21/16.
 */
public abstract class Primitive extends xLingual {

    private String mComment;

    public Primitive(String name, String internalName){
        super(name,internalName);

    }

    public Primitive(String name){
        super(name);

    }


    public String getComment() {
        return mComment;
    }

}
