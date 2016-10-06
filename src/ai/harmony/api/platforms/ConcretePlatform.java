package ai.harmony.api.platforms;

import ai.harmony.api.builder.FileObject;
import ai.harmony.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 9/7/16.
 */
public class ConcretePlatform implements SystemApi{
    protected String mAPI;
    List<FileObject> mFileObjects;


    public List<FileObject> build(Space s){
        return mFileObjects;
    }
    public ConcretePlatform() { mFileObjects = new ArrayList<>();}

    @Override
    public void setAPILevel(String name) {
        this.mAPI = name;
    }
}
