package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.tiers.Endpoint;

/**
 * Created by lauril on 1/23/17.
 */
public interface SystemAPI {

    Context send_data(byte[] data, Endpoint endpoint);

    void data_received(byte[] data, Endpoint endpoint);
}
