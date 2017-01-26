package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 10/6/16.
 */
public abstract class ConcreteLanguage {
    protected final List<FileObject> mFileObjects;

    protected ConcreteLanguage() {
        mFileObjects = new ArrayList<>();
    }

    public abstract List<FileObject> build(Space s, String buildPath);
}
