package org.stanford.ravel.rrt.tiers;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;


import org.stanford.ravel.rrt.utils.None;
import org.stanford.ravel.rrt.DispatcherAPI;
/**
 * Created by lauril on 1/23/17.
 */
public class Endpoint extends None{
    public enum TYPE { BLE, SQUARE, SOCKET, HTTP, GCM }

    private final int id;
    private final Endpoint.TYPE type;
    private boolean local = false;
    private volatile boolean mConnected = false;
    protected DispatcherAPI appDispatcher = null;

    Endpoint(Endpoint.TYPE type, int id) {
        this.type = type;
        this.id = id;
    }
    public TYPE getType() { return type; }
    public int getId() {
        return id;
    }

    public void setAppDispatcher(DispatcherAPI dapi ){
        this.appDispatcher = dapi;
    }

    public void connected() {
        mConnected = true;
        if ( this.appDispatcher != null && !local)
            appDispatcher.connected(this);
    }

    public void disconnected() {
        mConnected = false;
        if ( this.appDispatcher != null && !local) {
            appDispatcher.disconected(this);
        }

    }

    public boolean isConnected() {
        return mConnected;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }


    @Override
    public String toString() {
        return "[Type: " + this.type
                +", id: " + this.id
                + ", connected: " + mConnected
                + " local " + this.isLocal() + "]";
    }

    public static Endpoint fromString(int id, URI uri, Map<String, String> options) throws MalformedURLException {
        if (options == null)
            options = Collections.emptyMap();
        switch (uri.getScheme()) {
            case "ble":
                return new BleEndpoint(id);
            case "http":
            case "https":
                return new HttpEndpoint(id, uri, options.get("method"), options.get("user-agent"));
            case "tcp":
                return new TcpEndpoint(id, uri);
            default:
                throw new MalformedURLException("Invalid URI scheme " + uri.getScheme());
        }
    }
}
