package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.primitives.Space;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by lauril on 9/7/16.
 */
public class Django extends ConcretePlatform{
    private static Logger LOGGER = Logger.getLogger(Django.class.getName());

    @Override
    public List<FileObject> build(Space s, String buildPath) {
        return mFileObjects;
    }

    @Override
    public void setAPILevel(String name) {

    }
}
