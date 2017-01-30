package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.tiers.Endpoint;

/**
 * Created by lauril on 1/23/17.
 */
public interface ModelBottomAPI {

    void record_arrived(RavelPacket record, Endpoint endpoint);

    void record_departed(RavelPacket record, Endpoint endpoint);

    void record_saved_durably(RavelPacket record);

    void record_saved_endpoint(RavelPacket record, Endpoint endpoint);

    void endpoint_connected(Endpoint endpoint);

}
