package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * The API implemented by models which is called by the AppDispatcher
 * to report network events.
 *
 * Created by lauril on 1/23/17.
 */
public interface ModelBottomAPI {

    void recordArrived(RavelPacket record, Endpoint endpoint);

    void recordDeparted(RavelPacket record, Endpoint endpoint);

    void recordFailedToSend(RavelPacket record, Endpoint endpoint, Error error);

    void recordSavedDurably(RavelPacket record, Error error);

    void recordLoaded(RavelPacket record);
}
