package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.tiers.Endpoint;

/**
 * Created by lauril on 1/23/17.
 */
public interface ModelBottomAPI<RecordType> {

    void record_arrived(RavelPacket record, Endpoint endpoint);

    void record_departed(RecordType record, Endpoint endpoint);

    void record_saved_durably(RecordType record);

    void record_saved_endpoint(RecordType record, Endpoint endpoint);

    void endpoint_connected(Endpoint endpoint);

}
