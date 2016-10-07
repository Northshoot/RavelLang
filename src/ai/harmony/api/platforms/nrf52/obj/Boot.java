package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.platforms.RavelAPIObject;
import ai.harmony.api.platforms.RavelObjectInterface;

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
