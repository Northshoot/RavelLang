package org.stanford.api.lang;

import org.stanford.api.builder.FileObject;
import org.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 10/6/16.
 */
public class ConcreteLanguage {
    List<FileObject> mFileObjects;
    public ConcreteLanguage(){
        mFileObjects = new ArrayList<>();
    }

    public List<FileObject> build(Space s){
        return mFileObjects;
    }


}
