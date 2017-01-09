package org.stanford.api.platforms.nrf52.obj;

import org.stanford.api.builder.FileObject;
import org.stanford.api.platforms.RavelAPIObject;
import org.stanford.api.platforms.RavelObjectInterface;

import java.util.List;

/**
 * Created by lauril on 9/21/16.
 */
public class Boot  extends RavelAPIObject implements RavelObjectInterface {


    public Boot(String mBuildPath) {
        super();
    }

    @Override
    public String getHeaderDefName() {
        return null;
    }

    @Override
    public List<FileObject> getFiles() {
        return mFileList;
    }
}
