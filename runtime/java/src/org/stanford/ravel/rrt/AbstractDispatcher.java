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

    /******************* ******* event queue ***************************/
    private final ArrayBlockingQueue<Event> eventQueue = new ArrayBlockingQueue<Event>(5);

    protected synchronized void runNextEvent(Event e) {
        switch (e.getType()) {
            case DISPATCHER__STOP:
                this.stopped();
                queueEvent(new SystemEvent(Event.Type.DISPATCHER__QUIT));
                break;
            case DRIVER__DATA_RECEIVED:
                models__notifyArrived((NetworkEvent)e);
                break;
            case MODELS__NOTIFY_RECORD_DEPARTED:
                models__notifyDeparted((NetworkEvent)e);
                break;
            case MODELS__NOTIFY_RECORD_ARRIVED:
                break;

            case GENERIC__RUNNABLE:
                ((RunnableEvent)e).run();
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

    /***********************************************************************/
    /************** Network callbacks from Driver to AD ********************/
    /***********************************************************************/
    @Override
    public void driver__dataReceived(RavelPacket pkt, Endpoint endpoint) {
        NetworkEvent ne = new NetworkEvent(pkt, endpoint, Event.Type.DRIVER__DATA_RECEIVED);
        queueEvent(ne);
    }

    @Override
    public void driver__sendDone(Error networkError, RavelPacket data, Endpoint endpoint) {
        if (networkError == Error.SUCCESS) {
            NetworkEvent ne = new NetworkEvent(data, endpoint, networkError, Event.Type.MODELS__NOTIFY_RECORD_DEPARTED);
            queueEvent(ne);
        } else {
            LOGGER.info("Error sending to the server, status: " + networkError);
        }
    }
}
