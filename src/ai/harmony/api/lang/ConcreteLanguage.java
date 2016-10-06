package ai.harmony.api.lang;

import ai.harmony.api.builder.FileObject;
import ai.harmony.ravel.primitives.Space;

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
