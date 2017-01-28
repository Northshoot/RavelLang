package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 9/7/16.
 */
public abstract class ConcretePlatform implements SystemApi{
    protected String mAPI;
    protected final List<FileObject> mFileObjects;

    public abstract List<FileObject> build(Space s, String buildPath);
    public ConcretePlatform() { mFileObjects = new ArrayList<>();}

    @Override
    public void setAPILevel(String name) {
        this.mAPI = name;
    }
}
