package org.stanford.ravel.rrt.tiers;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

/**
 * Created by lauril on 1/23/17.
 */
public class Endpoint {
    public enum TYPE { BLE, SQUARE, SOCKET, HTTP, GCM }

    private final int id;
    private final Endpoint.TYPE type;
    private boolean local = false;
    private volatile boolean mConnected = false;

    Endpoint(Endpoint.TYPE type, int id) {
        this.type = type;
        this.id = id;
    }
    public TYPE getType() { return type; }
    public int getId() {
        return id;
    }

    public void connected() {
        mConnected = true;
    }

    public void disconnected() {
        mConnected = false;
    }

    public boolean isConnected() {
        return mConnected;
    }

    public boolean isLocal() {
        return local;
    }

    void setLocal(boolean local) {
        this.local = local;
    }


    @Override
    public String toString() {
        return "[Type: " + this.type
                +", id: " + this.id
                + ", connected: " + mConnected
                + "]";
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
