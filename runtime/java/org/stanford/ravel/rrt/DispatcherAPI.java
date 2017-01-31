package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by lauril on 1/23/17.
 */
public interface DispatcherAPI {
    Error send_data(RavelPacket data, Endpoint endpoint);

    void data_received(byte[] data, Endpoint endpoint);

    void driver_send_done(Error networkError, byte[] data, Endpoint endpoint);
}
