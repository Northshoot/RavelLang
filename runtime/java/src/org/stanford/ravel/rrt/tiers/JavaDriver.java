package org.stanford.ravel.rrt.tiers;

import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.DriverAPI;
import org.stanford.ravel.rrt.RavelPacket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * An implementation of DriverAPI that supports TCP/IP
 *
 * Created by lauril on 1/23/17.
 */
public class JavaDriver implements DriverAPI {
    private final Map<String, Set<Endpoint>> endpointsMap = new HashMap<>();

    private final DispatcherAPI appDispatcher;

    private final Executor threadPool;
    private final Map<TcpEndpoint, RavelSocket> socketClients = new HashMap<>();

    public JavaDriver(DispatcherAPI appDispatcher) {
        threadPool = Executors.newCachedThreadPool();
        this.appDispatcher = appDispatcher;
    }

    private RavelSocket getSocket(TcpEndpoint endpoint) throws RavelIOException {
        synchronized (socketClients) {
            if (socketClients.containsKey(endpoint))
                return socketClients.get(endpoint);

            RavelSocketClient client = new RavelSocketClient(appDispatcher.getAppName(), endpoint, appDispatcher);
            socketClients.put(endpoint, client);
            return client;
        }
    }

    void registerSocket(TcpEndpoint endpoint, RavelSocket socket) {
        internalRegisterEndpoint(endpoint);

        synchronized (socketClients) {
            if (socketClients.containsKey(endpoint)) {
                try {
                    socketClients.get(endpoint).close();
                } catch(IOException e) {
                    System.err.println("Failed to close existing socket to " + endpoint.getAddress() + ":" + endpoint.getPort());
                    e.printStackTrace(System.err);
                }
            }
            socketClients.put(endpoint, socket);
        }
    }

    void removeSocket(TcpEndpoint endpoint) {
        synchronized (socketClients) {
            socketClients.remove(endpoint);
        }
    }

    protected void sendDataThread(RavelPacket data, Endpoint endpoint) throws RavelIOException {
        switch (endpoint.getType()) {
            case SOCKET:
                RavelSocket socket = getSocket((TcpEndpoint)endpoint);
                socket.write(data);
                break;

            case HTTP:
                HttpClient client = new HttpClient(endpoint);
                client.write(data);
                break;

            default:
                appDispatcher.driver__sendDone(Error.ENDPOINT_UNREACHABLE, data, endpoint);
        }
    }

    @Override
    public Error sendData(final RavelPacket data, final Endpoint endpoint) {
        if (endpoint.isLocal()) // can't send to one self
            return Error.SYSTEM_ERROR;

        threadPool.execute(new Runnable() {
            public void run() {
                try {
                    sendDataThread(data, endpoint);
                    appDispatcher.driver__sendDone(Error.SUCCESS, data, endpoint);
                } catch(RavelIOException e) {
                    appDispatcher.driver__sendDone(e.getError(), data, endpoint);
                }
            }
        });

        // all errors are reported asynchronously
        return Error.IN_TRANSIT;
    }

    private Set<Endpoint> getEndpointsSet(String name) {
        Set<Endpoint> s;
        if (!endpointsMap.containsKey(name)) {
            s = new HashSet<>();
            endpointsMap.put(name, s);
        } else {
            s = endpointsMap.get(name);
        }
        return s;
    }

    @Override
    public Collection<Endpoint> getEndpointsByName(String name) {
        synchronized (endpointsMap) {
            // Make a copy of the associated endpoint set so that we can pass it outside
            // the synchronized block without fear for concurrent modification
            return Collections.unmodifiableCollection(new ArrayList<>(getEndpointsSet(name)));
        }
    }

    protected Error startLocalEndpoint(Endpoint ep) {
        try {
            switch (ep.getType()) {
                case SOCKET:
                    new RavelSocketServer((TcpEndpoint)ep, this, appDispatcher);
                    break;
                default:
                    return Error.ENDPOINT_UNREACHABLE;
            }

            return Error.SUCCESS;
        } catch(RavelIOException e) {
            return e.getError();
        }
    }

    protected Error startRemoteEndpoint(Endpoint ep) {
        try {
            // make sure we register the endpoint as client, by attempting the connection
            // immediately
            switch (ep.getType()) {
                case SOCKET:
                    getSocket((TcpEndpoint) ep);
                    break;

                case HTTP:
                    return Error.SUCCESS;

                default:
                    return Error.ENDPOINT_UNREACHABLE;
            }
        } catch(RavelIOException e) {
            // ignore the error if the connection fails, we'll try at a later time
            // and see if it fails again
        }
        return Error.SUCCESS;
    }

    private void internalRegisterEndpoint(Endpoint ep) {
        synchronized (endpointsMap) {
            getEndpointsSet(ep.getName()).add(ep);
            ep.setLocal(ep.getName().equals(appDispatcher.getAppName()));
        }
    }

    @Override
    public Error registerEndpoint(Endpoint ep) {
        internalRegisterEndpoint(ep);

        if (ep.isLocal())
            return startLocalEndpoint(ep);
        else
            return startRemoteEndpoint(ep);
    }

    @Override
    public void appDispatcherReady() {
        appDispatcher.started();
    }

    @Override
    public void saveDurably(RavelPacket packet) {
        // TODO
        appDispatcher.driver__savedDurably(packet, Error.SUCCESS);
    }
}
