package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by lauril on 1/31/17.
 */
public abstract class AbstractDispatcher implements DispatcherAPI, SystemEventAPI  {
    private static final Logger LOGGER = Logger.getLogger(AbstractDispatcher.class.getName());

    private Thread loopThread;

    protected AbstractDispatcher() {
        loopThread = new Thread(this::eventLoop);
        loopThread.setDaemon(true);
        loopThread.start();
    }

    public void stop() {
        pushEvent(new SystemEvent(Event.Type.DISPATCHER__QUIT));
        try {
            loopThread.join(10000);
        } catch(InterruptedException e) {
            LOGGER.severe("Failed to quit after 10s");
        }
    }

    /***********************************************************************/
    /*************** Callbacks to the model to AD **************************/
    /***********************************************************************/
    protected abstract void models__notifyDeparted(Event event);

    protected abstract void models__notifyArrived(Event event);

    /******************* ******* event queue ***************************/
    private final ArrayBlockingQueue<Event> eventQueue = new ArrayBlockingQueue<Event>(5);

    protected synchronized void runNextEvent(Event e) {
        switch (e.getType()) {
            case DRIVER__DATA_RECEIVED:
                models__notifyArrived(e);
                break;
            case MODELS__NOTIFY_RECORD_DEPARTED:
                models__notifyDeparted(e);
                break;
            case DRIVER__SEND_DATA:
                driver__sendData(e);
                break;
            case MODELS__NOTIFY_RECORD_ARRIVED:
                break;
        }
    }

    private void eventLoop() {
        LOGGER.info("Dispatcher event loop started");

        try {
            while (true) {
                Event e = eventQueue.take();
                runNextEvent(e);
                if (e.getType() == Event.Type.DISPATCHER__QUIT)
                    return;
            }
        } catch(InterruptedException e) {
            // nothing to do if interrupted
        }
    }

    private void pushEvent(Event event) {
        eventQueue.offer(event);
    }

    /***********************************************************************/
    /*************** AD Commands from model to AD **************************/
    /***********************************************************************/

    @Override
    public Error model__sendData(RavelPacket pkt, Endpoint endpoint) {
        // FIXME set src and dest
        NetworkEvent ne = new NetworkEvent(pkt.toBytes(), endpoint, Event.Type.DRIVER__SEND_DATA);
        eventQueue.offer(ne);
        return Error.SUCCESS;
    }

    /***********************************************************************/
    /************** Network callbacks from Driver to AD ********************/
    /***********************************************************************/
    @Override
    public void driver__dataReceived(byte[] data, Endpoint endpoint) {
        NetworkEvent ne = new NetworkEvent(data, endpoint, Event.Type.DRIVER__DATA_RECEIVED);
        pushEvent(ne);
    }

    @Override
    public void driver__sendDone(Error networkError, byte[] data, Endpoint endpoint) {
        NetworkEvent ne = new NetworkEvent(data, endpoint, networkError, Event.Type.MODELS__NOTIFY_RECORD_DEPARTED);
        pushEvent(ne);
    }
}
