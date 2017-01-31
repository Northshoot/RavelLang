package org.stanford.ravel.rrt.tiers;

import org.stanford.ravel.rrt.AbstractDispatcher;
import org.stanford.ravel.rrt.DriverAPI;
import org.stanford.ravel.rrt.SocketServer;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;
import patterns.src.java.rrt.RavelSocketProtocol;


import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by lauril on 1/23/17.
 */
public class AndroidDriver implements DriverAPI {

    Map<String, Endpoint> endpointsMap = new HashMap<>();
    private AbstractDispatcher appDispatcher;
    private SocketServer ss;
    private RavelSocketProtocol rsp;
    private Socket clientSocket;

    public AndroidDriver(AbstractDispatcher appDispatcher){
        this.appDispatcher = appDispatcher;
    }
    void pprint(String s){
        //TODO
        System.out.println("[" + appDispatcher.getName() + "::AndroidDriver]>" + s);
    }
    public Error sendData(byte[] data, Endpoint endpoint) {
        //send data to the right channel
        if(endpoint.getType() == Endpoint.TYPE.SOCKET){
                //TODO: mmmrm not the best way to keep reconnecting
                new Thread(new Runnable() {
                    public void run() {
                        try {
                        clientSocket = new Socket(endpoint.getAddress(), endpoint.getPort());
                        clientSocket.getOutputStream().write(data);
                        clientSocket.close();
                        appDispatcher.driver_send_done(Error.SUCCESS, data, endpoint);
                        } catch (IOException e) {
                            endpoint.setDisconnected();
                            appDispatcher.driver_send_done(Error.NETWORK_ERROR, data, endpoint);
                        }
                    }
                }).start();
                return Error.SUCCESS;

        }
        return Error.WRITE_ERROR;
    }

    public void register_endpoint(Endpoint endpoint){
        endpointsMap.put(endpoint.getName(), endpoint);
    }


    public Timer get_timer(){
        return new Timer();
    }

    public void rx_data_from_ble(byte[] data){
        //need a function that maps rx to endpoint
        //in a more clean way
        for (Endpoint e: endpointsMap.values()) {
            if(e.getType() == Endpoint.TYPE.BLE){
                appDispatcher.data_received(data, e);
            }
        }
    }

    public void rx_data_from_cloud(byte[] data){
        for (Endpoint e: endpointsMap.values()) {
            if(e.getType() == Endpoint.TYPE.HTTP){
                appDispatcher.data_received(data, e);
            }
        }
    }

    public void rx_data_from_socket(byte[] bytes, int count){
        for (Endpoint e: endpointsMap.values()) {
            if(e.getType() == Endpoint.TYPE.SOCKET){
                appDispatcher.data_received(bytes, e);
            }
        }
    }

    public void rx_data_from_gcm(byte[] data){
        for (Endpoint e: endpointsMap.values()) {
            if(e.getType() == Endpoint.TYPE.GCM){
                appDispatcher.data_received(data, e);
            }
        }
    }

    @Override
    public void appDispatcherReady() {
        //TODO: clean out for generations
        if(appDispatcher.getName() == "GTW"){
            rsp = new RavelSocketProtocol(this);
            ss = new SocketServer(4444, rsp);
            ss.run();
        }
        appDispatcher.started();
    }
}
