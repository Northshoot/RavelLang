package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.events.Event;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by lauril on 1/23/17.
 */
public interface DispatcherAPI {

    String getAppName();

    void driver__dataReceived(byte[] data, Endpoint endpoint);

    void driver__sendDone(int status,Error networkError, byte[] data, Endpoint endpoint);

    void driver__sendData(Event event);

    /***********************************************************************/
    /*************** AD Commands from model to AD **************************/
    /***********************************************************************/
    Endpoint getEndpointByName(String name);

    Error model__sendData(RavelPacket data, Endpoint endpoint);
}
