package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by lauril on 1/23/17.
 */
public interface DispatcherAPI {

    void runNextEvent();

    void driver__dataReceived(byte[] data, Endpoint endpoint);

    void driver__sendDone(Error networkError, byte[] data, Endpoint endpoint);


    /***********************************************************************/
    /*************** AD Commands from model to AD **************************/
    /***********************************************************************/
    Error model__sendData(RavelPacket data, Endpoint endpoint);

    /***********************************************************************/
    /*************** Callbakcs to the model to AD **************************/
    /***********************************************************************/
    void models__notifyDeparted();

    void models__notifyArrived();
    void models__notifySendDone();


}
