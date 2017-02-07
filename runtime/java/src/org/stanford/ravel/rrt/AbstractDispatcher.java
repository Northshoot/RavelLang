package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.events.Event;
import org.stanford.ravel.rrt.events.NetworkEvent;
import org.stanford.ravel.rrt.events.SystemEvent;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;
import org.stanford.ravel.rrt.utils.HttpStatus;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by lauril on 1/31/17.
 */
public abstract class AbstractDispatcher implements DispatcherAPI, SystemEventAPI  {
    private static final Logger LOGGER = Logger.getLogger(AbstractDispatcher.class.getName());

    private Thread loopThread;

    protected AbstractDispatcher() {
        loopThread = new Thread(new Runnable() {
            @Override
            public void run() {
                eventLoop();
            }
        });
        loopThread.setDaemon(true);
        loopThread.start();
    }

    public void stop() {
        pushEvent(new SystemEvent(Event.Type.DISPATCHER__STOP));
        try {
            loopThread.join(10000);
        } catch(InterruptedException e) {
            LOGGER.severe("Failed to quit after 10s");
        }
    }

    /***********************************************************************/
    /*************** Callbacks to form the AD to the model *****************/
    /***********************************************************************/
    protected abstract void models__notifyDeparted(Event event);


    protected abstract void models__notifyArrived(Event event);

    protected abstract void models__notifyFull(Event e);

    /******************* ******* event queue ***************************/
    private final ArrayBlockingQueue<Event> eventQueue = new ArrayBlockingQueue<Event>(5);

    protected synchronized void runNextEvent(Event e) {
        switch (e.getType()) {
            case DISPATCHER__STOP:
                this.stopped();
                pushEvent(new SystemEvent(Event.Type.DISPATCHER__QUIT));
                break;
            case DRIVER__DATA_RECEIVED:
                models__notifyArrived(e);
                break;
            case MODELS__NOTIFY_RECORD_DEPARTED:
                models__notifyDeparted(e);
                break;
            case DRIVER__SEND_DATA:
                driver__sendData(e);
                break;
            case MODEL__NOTIFY_FULL:
                models__notifyFull(e);
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
                if (e.getType() == Event.Type.DISPATCHER__QUIT)
                    return;
                runNextEvent(e);
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
    /*************** AD Commands from controller to AD *********************/
    /***********************************************************************/

    @Override
    public void print(String msg) {
        System.err.println(msg);
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
    public void driver__sendDone(int status, Error networkError, byte[] data, Endpoint endpoint) {
        if(status == 200) {
            NetworkEvent ne = new NetworkEvent(data, endpoint, networkError, Event.Type.MODELS__NOTIFY_RECORD_DEPARTED);
            pushEvent(ne);
        } else {
            LOGGER.info("Error sending to the server, status: " + HttpStatus.getError(status));
        }
    }
}
