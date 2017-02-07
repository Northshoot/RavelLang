package org.stanford.ravel.rrt.android;

import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.DriverAPI;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by gcampagn on 2/6/17.
 */
public class AndroidDriver implements DriverAPI {
    private final DispatcherAPI mDispatcher;

    public AndroidDriver(DispatcherAPI dispatcher) {
        mDispatcher = dispatcher;
    }

    @Override
    public void appDispatcherReady() {
        mDispatcher.started();
    }

    @Override
    public Error sendData(byte[] data, Endpoint ep) {
        return null;
    }
}
