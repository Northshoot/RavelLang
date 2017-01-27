package patterns.src.java.tiers;

import patterns.src.java.model.Model;
import patterns.src.java.app.AppDispatcher;
import patterns.src.java.rrt.*;


import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by lauril on 1/23/17.
 */
public class AndroidDriver extends Driver{

    Map<String, Endpoint> endpointsMap = new HashMap<>();
    private AppDispatcher appDispatcher;
    private SocketServer ss;
    private RavelSocketProtocol rsp;
    private Socket clientSocket;

    public AndroidDriver(AppDispatcher appDispatcher){
        this.appDispatcher = appDispatcher;


    }
    public Context send_data(byte[] data, Endpoint endpoint) {
        //send data to the right channel
        if(endpoint.getType() == Endpoint.TYPE.SOCKET){
            try {
                //TODO: mmmrm not the best way to keep reconnecting
                clientSocket = new Socket(endpoint.getAddress(), endpoint.getPort());
                clientSocket.getOutputStream().write(data);
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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
        appDispatcher.started();
        if(appDispatcher.getName() == "GTW"){
            rsp = new RavelSocketProtocol(this);
            ss = new SocketServer(4444, rsp);
            ss.run();
        }
    }
}
