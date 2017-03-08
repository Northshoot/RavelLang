package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

import java.util.Collection;

/**
 * Created by lauril on 1/23/17.
 */
public interface DriverAPI {
    void appDispatcherReady();

    void loadDurableStorage();

    Error sendData(RavelPacket pkt, Endpoint ep);
    void saveDurably(RavelPacket pkt);
    void deleteFromDurableStorage(int modelId, int recordId);

    /**
     * Get all the known connected endpoints with
     * @param name
     * @return
     */
    Collection<Endpoint> getEndpointsByName(String name);

    /**
     * Register a remote endpoint;
     *
     * If the endpoint is remote, this might attempt an initial connection
     * to the endpoint. If the endpoint is local it will set up any listening
     * connections / sockets.
     *
     * @param ep the endpoint
     * @return Error.SUCCESS if the registration succeeded, or an error code
     */
    Error registerEndpoint(Endpoint ep);
}
