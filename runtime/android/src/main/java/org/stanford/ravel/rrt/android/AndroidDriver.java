package org.stanford.ravel.rrt.android;

import android.app.Service;
import org.stanford.ravel.rrt.DriverAPI;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by gcampagn on 2/6/17.
 */
public class AndroidDriver implements DriverAPI {
    private Service mService;


    @Override
    public void appDispatcherReady() {

    }

    @Override
    public Error sendData(byte[] data, Endpoint ep) {
        return null;
    }
}
