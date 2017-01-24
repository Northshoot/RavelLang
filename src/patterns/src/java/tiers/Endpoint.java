package patterns.src.java.tiers;

import patterns.src.java.rrt.Driver;

/**
 * Created by lauril on 1/23/17.
 */
public class Endpoint {

    public enum TYPE { BLE, SQUARE, SOCKET, HTTP, HTTPS, GCM }

    private String mName;
    private boolean mConnected = false;
    private Endpoint.TYPE mType;

    public Endpoint(String name, Endpoint.TYPE type){
        this.mType = type;
        this.mName = name;
    }

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


}
