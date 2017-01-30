package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.primitives.InstantiatedController;
import org.stanford.ravel.primitives.InstantiatedModel;
import org.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by gcampagn on 1/29/17.
 */
public abstract class BaseLanguage implements ConcreteLanguage {
    private static Logger LOGGER = Logger.getLogger(CLang.class.getName());

    protected final List<FileObject> mFileObjects;
    private String buildPath;

    protected BaseLanguage() {
        mFileObjects = new ArrayList<>();
    }

    protected void addModule(CodeModule module) {
        mFileObjects.addAll(module.getFiles());
    }
    protected void addFile(FileObject fo) {
        mFileObjects.add(fo);
    }

    // should be abstract, but CLang...
    protected CodeModule createModel(InstantiatedModel im) {
        return null;
    }
    protected CodeModule createController(InstantiatedController ictr) {
        return null;
    }
    protected void createModels(Space s) {
        for (InstantiatedModel im : s.getModels())
            addModule(createModel(im));
    }
    protected void createControllers(Space s) {
        for (InstantiatedController ictr : s.getControllers())
            addModule(createController(ictr));
    }
    protected abstract void createDispatcher(Space s);

    public List<FileObject> build(Space s) {
        this.buildPath = buildPath;
        LOGGER.info("Building Space: " +s.mName);
        createModels(s);
        createControllers(s);
        createDispatcher(s);
        return mFileObjects;
    }
}
