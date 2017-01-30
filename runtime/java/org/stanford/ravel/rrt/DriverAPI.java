package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by lauril on 1/23/17.
 */
public interface DriverAPI {
    void appDispatcherReady();

    Error sendData(byte[] data, Endpoint ep);

    /**
     * Get system timer
     */


    /**
     * Register endpoint
     */

}
