package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.events.Event;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;
import org.stanford.ravel.rrt.tiers.RavelIdentity;

import java.util.Collection;

/**
 * Created by lauril on 1/23/17.
 */
public interface DispatcherAPI extends SystemEventAPI {
    String getAppName();

    int getAppId();

    int getDeviceId();
    RavelIdentity getIdentity();
    /***********************************************************************/
    /*************** AD Commands from driver to AD *************************/
    /***********************************************************************/

    void driver__dataReceived(RavelPacket data, Endpoint endpoint);

    void driver__sendDone(Error networkError, RavelPacket data, Endpoint endpoint);

    void driver__savedDurably(RavelPacket data, Error error);

    void driver__loadFromStorage(RavelPacket data);

    /***********************************************************************/
    /*************** AD Commands from model to AD **************************/
    /***********************************************************************/

    Collection<Endpoint> getEndpointsBySrc(int id);

    Collection<Endpoint> getEndpointsByTier(int tier);

    Error model__sendData(RavelPacket data, Endpoint endpoint);
    void model__saveDurably(RavelPacket data);
    void model__deleteFromStorage(int modelId, int recordId);

    void queueEvent(Event event);

    KeyProvider getKeyProvider();
}
