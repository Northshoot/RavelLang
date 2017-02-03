package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lauril on 9/7/16.
 */
public abstract class BasePlatform implements SystemApi, ConcretePlatform {
    // FIXME should be private
    protected String mAPI;
    // FIXME should be private
    protected final List<FileObject> mFileObjects;

    protected BasePlatform() {
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

    @Override
    public OptionParser getOptions() {
        return new PlatformOptions();
    }

    // FIXME should be abstract, but nrf52platform...
    protected CodeModule createLauncher() {
        return null;
    }
    protected CodeModule createBuildSystem(Space s, List<FileObject> files) {
        return null;
    }

    public List<FileObject> createBuildSystem(List<FileObject> files) {
        return Collections.emptyList();
    }

    @Override
    public List<FileObject> build(Space s) {
        addModule(createLauncher());
        addModule(createBuildSystem(s, mFileObjects));
        return mFileObjects;
    }

    @Override
    public void setAPILevel(String name) {
        this.mAPI = name;
    }
}
