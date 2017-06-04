package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.events.Event;
import org.stanford.ravel.rrt.events.NetworkEvent;
import org.stanford.ravel.rrt.events.RunnableEvent;
import org.stanford.ravel.rrt.events.SystemEvent;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by lauril on 1/31/17.
 */
public abstract class AbstractDispatcher implements DispatcherAPI {
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
        loopThread.setName("AppDispatcher: " + getAppName());
        loopThread.start();
    }

    public void stop() {
        queueEvent(new SystemEvent(Event.Type.DISPATCHER__STOP));
        try {
            loopThread.join(10000);
        } catch(InterruptedException e) {
            LOGGER.severe("Failed to quit after 10s");
        }
    }

    /***********************************************************************/
    /*************** Callbacks to form the AD to the model *****************/
    /***********************************************************************/
    protected abstract void models__notifyDeparted(NetworkEvent event);

    protected abstract void models__notifyArrived(NetworkEvent event);

    protected abstract void models__notifyFailedToSend(NetworkEvent event);

    protected abstract void models__notifySavedDurably(NetworkEvent event);

    protected abstract void models__notifyLoadFromStorage(NetworkEvent event);

    /******************* ******* event queue ***************************/
    private final ArrayBlockingQueue<Event> eventQueue = new ArrayBlockingQueue<>(20);

    private void runNextEvent(Event e) {
        switch (e.getType()) {
            case DISPATCHER__STOP:
                this.stopped();
                // queue a new event so that we process all events in the queue up to
                // here
                queueEvent(new SystemEvent(Event.Type.DISPATCHER__QUIT));
                break;
            case DRIVER__DATA_RECEIVED:
                models__notifyArrived((NetworkEvent)e);
                break;
            case MODELS__NOTIFY_RECORD_DEPARTED:
                models__notifyDeparted((NetworkEvent)e);
                break;
            case MODELS__NOTIFY_RECORD_FAILED_TO_SEND:
                models__notifyFailedToSend((NetworkEvent)e);
                break;
            case DRIVER__SAVED_DURABLY:
                models__notifySavedDurably((NetworkEvent)e);
                break;

            case GENERIC__RUNNABLE:
                ((RunnableEvent)e).run();
                break;
        }
    }

    private synchronized void eventLoop() {
        LOGGER.info(getAppName() + ": dispatcher event loop started");

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

    @Override
    public void queueEvent(Event event) {
        eventQueue.offer(event);
    }

    /***********************************************************************/
    /*************** AD Commands from controller to AD *********************/
    /***********************************************************************/

    @Override
    public void print(String msg) {
        System.err.println(msg);
    }

    public int deviceID() {
        return this.getDeviceId();
    }

    /***********************************************************************/
    /************** Network callbacks from Driver to AD ********************/
    /***********************************************************************/
    @Override
    public void  driver__dataReceived(RavelPacket pkt, Endpoint endpoint) {
        NetworkEvent ne = new NetworkEvent(pkt, endpoint, Event.Type.DRIVER__DATA_RECEIVED);
        System.out.println("DOPE: driver__dataReceived: " + pkt.toString());
        queueEvent(ne);
    }

    @Override
    public void driver__sendDone(Error networkError, RavelPacket data, Endpoint endpoint) {
        if (networkError != Error.SUCCESS) {
            LOGGER.info("Error sending to the server, status: " + networkError);
        }
        NetworkEvent ne = new NetworkEvent(data, endpoint, networkError,
                networkError == Error.SUCCESS ? Event.Type.MODELS__NOTIFY_RECORD_DEPARTED : Event.Type.MODELS__NOTIFY_RECORD_FAILED_TO_SEND);
        queueEvent(ne);
    }

    @Override
    public void driver__savedDurably(RavelPacket data, Error error) {
        NetworkEvent ne = new NetworkEvent(data, null, error, Event.Type.DRIVER__SAVED_DURABLY);
        queueEvent(ne);
    }

    @Override
    public void driver__loadFromStorage(RavelPacket data) {
        NetworkEvent ne = new NetworkEvent(data, null, null, Event.Type.DRIVER__LOAD_FROM_STORAGE);
        // don't queue the event, run it right away, so the models are fully loaded before system started
        models__notifyLoadFromStorage(ne);
    }
}
