package org.stanford.ravel.api.platforms;

import org.antlr.v4.runtime.misc.Pair;
import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.platforms.nrf52.NrfPlatformOptions;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lauril on 9/7/16.
 */
public abstract class BasePlatform implements SystemApi, ConcretePlatform {
    private final int mMinAPI, mMaxAPI;
    private int mAPI;

    private final List<FileObject> mFileObjects = new ArrayList<>();

    protected BasePlatform() {
        this(0, Integer.MAX_VALUE);
    }

    protected BasePlatform(int minApi, int maxApi) {
        mMinAPI = minApi;
        mMaxAPI = maxApi;
    }

    protected void addModule(CodeModule module) {
        if (module == null)
            return;
        mFileObjects.addAll(module.getFiles());
    }
    protected void addFile(FileObject fo)
    {
        mFileObjects.add(fo);
    }



    // FIXME should be abstract, but nrf52platform...
    protected CodeModule createLauncher(Space s) {
        return null;
    }

    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        return Collections.emptyList();
    }

    @Override
    public List<FileObject> build(Space s) {
        addModule(createLauncher(s));
        return mFileObjects;
    }

    protected int getAPILevel() {
        return mAPI;
    }

    @Override
    public void setAPILevel(int level) {
        if (level < mMinAPI)
            throw new IllegalArgumentException("Invalid API version, must be >= " + mMinAPI);
        if (level > mMaxAPI)
            throw new IllegalArgumentException("Invalid API version, must be <= " + mMaxAPI);
        this.mAPI = level;
    }


}
