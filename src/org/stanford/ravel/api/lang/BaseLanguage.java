package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.primitives.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by gcampagn on 1/29/17.
 */
public abstract class BaseLanguage implements ConcreteLanguage {
    private static Logger LOGGER = Logger.getLogger(CLang.class.getName());

    // FIXME: make private when CLang is fixed
    protected final List<FileObject> mFileObjects;

    protected BaseLanguage() {
        mFileObjects = new ArrayList<>();
    }

    protected void addModule(CodeModule module) {
        if (module == null)
            return;
        mFileObjects.addAll(module.getFiles());
    }
    protected void addFile(FileObject fo) {
        mFileObjects.add(fo);
    }

    protected abstract CodeModule createModel(ConcreteModel im);
    protected abstract CodeModule createController(ConcreteController ictr);
    protected abstract CodeModule createView(ConcreteView iview);
    protected abstract CodeModule createInterface(ConcreteInterface iiface);

    private void createModels(Space s) {
        for (ConcreteModel im : s.getModels())
            addModule(createModel(im));
    }
    private void createControllers(Space s) {
        for (ConcreteController ictr : s.getControllers())
            addModule(createController(ictr));
    }
    private void createInterfaces(Space s) {
        for (ConcreteInterface iiface : s.getInterfaces())
            addModule(createInterface(iiface));
    }
    private void createViews(Space s) {
        for (ConcreteView iview : s.getViews())
            addModule(createView(iview));
    }
    protected abstract CodeModule createDispatcher(Space s);

    public List<FileObject> build(Space s) {
        LOGGER.info("Building Space: " +s.getName());
        createModels(s);
        createControllers(s);
        createInterfaces(s);
        createViews(s);
        addModule(createDispatcher(s));
        return mFileObjects;
    }
}
