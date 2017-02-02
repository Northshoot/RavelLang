package org.stanford.ravel.rrt.tiers;

/**
 * Created by lauril on 1/23/17.
 */
public class Endpoint {
    public enum TYPE { BLE, SQUARE, SOCKET, HTTP, HTTPS, GCM }


    protected String name = null;
    protected int port = -1;
    protected String base = null;
    protected String method = null;
    protected String url = null;
    protected Endpoint.TYPE type = null;
    protected String USER_AGENT = null;


    protected boolean mConnected = false;

    public Endpoint() {

    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return base;
    }

    public String getUrl() { return url;}

    public String setUrl(String url) { this.url = url;  }

    public TYPE getType() { return type; }

    public String getName(){ return  name; }

    public void connected(){
        mConnected = true;
    }

    public void disconnected(){
        mConnected = false;
    }

    public boolean isConnected(){ return mConnected; }


    @Override
    public String toString() {
        return "[Type: " + this.type
                +", name: " + this.name
                + ", connected: " + mConnected
                + ", addr: " + this.base +this.url
                + "]";
    }
}
