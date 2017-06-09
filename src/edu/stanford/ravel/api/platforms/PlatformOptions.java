package edu.stanford.ravel.api.platforms;

import edu.stanford.ravel.api.InvalidOptionException;
import edu.stanford.ravel.api.OptionParser;

/**
 * Created by gcampagn on 1/31/17.
 */
public class PlatformOptions implements OptionParser {
    private boolean debug = false;
    protected String runtimePath;
    protected String path;

    public PlatformOptions(String runtimePath, String path) {
        this.runtimePath = runtimePath;
        this.path = path;
    }

    public boolean getDebug() { return this.debug; }

    public String getPlatformPath() {
        return path;
    }

    public String getRuntimePath() {
        return runtimePath;
    }

    @Override
    public boolean setOption(String prefix, String option, String value) throws InvalidOptionException {
        if (!prefix.equals("platform"))
            return false;
        switch (option){
            case "debug":
                debug = Boolean.valueOf(value);
        }
        throw new InvalidOptionException("Unrecognized option -Xplatform:" + option);
    }
}
