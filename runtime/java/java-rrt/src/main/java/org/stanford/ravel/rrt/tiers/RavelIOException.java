package org.stanford.ravel.rrt.tiers;

import java.io.IOException;

/**
 * Created by gcampagn on 2/8/17.
 */
public class RavelIOException extends Exception {
    private final Error error;

    public RavelIOException(Error error) {
        this.error = error;
    }

    public RavelIOException(IOException cause) {
        super(cause);
        this.error = Error.NETWORK_ERROR;
    }

    public Error getError() {
        return error;
    }
}
