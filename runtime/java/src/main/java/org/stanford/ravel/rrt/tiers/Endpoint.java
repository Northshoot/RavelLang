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

    private final String name;
    private final Endpoint.TYPE type;
    private boolean local = false;
    private volatile boolean mConnected = false;

    Endpoint(Endpoint.TYPE type) {
        this(type, null);
    }
    Endpoint(Endpoint.TYPE type, String name) {
        this.type = type;
        this.name = name;
    }
    public TYPE getType() { return type; }
    public String getName() {
        return  name;
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
                +", name: " + this.name
                + ", connected: " + mConnected
                + "]";
    }

    public static Endpoint fromString(String name, URI uri, Map<String, String> options) throws MalformedURLException {
        if (options == null)
            options = Collections.emptyMap();
        switch (uri.getScheme()) {
            case "ble":
                return new BleEndpoint(name);
            case "http":
            case "https":
                return new HttpEndpoint(name, uri, options.get("method"), options.get("user-agent"));
            case "tcp":
                return new TcpEndpoint(name, uri);
            default:
                throw new MalformedURLException("Invalid URI scheme " + uri.getScheme());
        }
    }
}
