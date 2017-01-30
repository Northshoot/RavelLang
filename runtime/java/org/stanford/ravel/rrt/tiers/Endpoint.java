package org.stanford.ravel.rrt.tiers;

/**
 * Created by lauril on 1/23/17.
 */
public class Endpoint {

    public Endpoint(String name, TYPE socket, String address, int port) {
        this(name, socket);
        this.mPort = port;
        this.mAddress = address;
    }

    public int getPort() {
        return mPort;
    }

    public String getAddress() {
        return mAddress;
    }

    public enum TYPE { BLE, SQUARE, SOCKET, HTTP, HTTPS, GCM }

    private String mName;
    //TODO: deal with setters and getters
    private boolean mConnected = true;
    private Endpoint.TYPE mType;
    private String mAddress;
    private int mPort;

    public Endpoint(String name, Endpoint.TYPE type){
        this.mType = type;
        this.mName = name;
    }

    public void setAddress(String address){ this.mAddress = address; }
    public TYPE getType() { return mType; }
    public String getName(){ return  mName; }
    public void setConnected(){
        mConnected = true;
    }

    public void setDisconnected(){
        mConnected = false;
    }

    public boolean isConnected(){ return mConnected; }

    public void write(byte[] data){
        System.out.println("Writes to endpoint: " + mName);
    }

    @Override
    public String toString() {
        return "[Type: " + this.mType
                +", name: " + this.mName
                + ", connected: " + mConnected
                + ", addr: " + this.mAddress
                + "]";
    }
}
