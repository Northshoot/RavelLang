package patterns.src.java.tiers;

import patterns.src.java.model.Record;
import patterns.src.java.rrt.AppDispatcher;
import patterns.src.java.rrt.Context;
import patterns.src.java.rrt.Driver;



import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by lauril on 1/23/17.
 */
public class AndroidDriver extends Driver{

    Map<String, Endpoint> endpointsMap = new HashMap<>();
    private AppDispatcher appDispatcher;

    public AndroidDriver(AppDispatcher appDispatcher){
        this.appDispatcher = appDispatcher;
        appDispatcher.setDriver(this);
    }
    public Context send_data(Record record, Endpoint endpoint) {
        //send data to the right channel
        return null;
    }

    public void register_endpoint(Endpoint endpoint){
        endpointsMap.put(endpoint.getName(), endpoint);
    }


    public Timer get_timer(){
        return new Timer();
    }

    public void rx_data_from_ble(byte[] data){
        for (Endpoint e: endpointsMap.values()) {
            if(e.getType() == Endpoint.TYPE.BLE){
                appDispatcher.data_received(data, e);
            }
        }
    }

    public void rx_data_from_cloud(byte[] data){

    }

    public void rx_data_from_socket(byte[] data){
        for (Endpoint e: endpointsMap.values()) {
            if(e.getType() == Endpoint.TYPE.SOCKET){
                appDispatcher.data_received(data, e);
            }
        }
    }
}
