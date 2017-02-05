package patterns.src.java.app;

import patterns.src.java.controller.ModelController;
import patterns.src.java.model.Model;
import patterns.src.java.rrt.Context;
import patterns.src.java.rrt.DispatherAPI;
import patterns.src.java.rrt.RavelPacket;
import patterns.src.java.rrt.SystemEventAPI;
import patterns.src.java.tiers.AndroidDriver;
import patterns.src.java.tiers.Endpoint;


import java.util.Map;

/**
 * Created by lauril on 1/23/17.
 */
public class AppDispatcher implements DispatherAPI, SystemEventAPI {


    //AUTOGEN
    Model model_id_1 ;
    ModelController mcntr_id_1 ;

    Map<String, Endpoint> mEndpoints;

    AndroidDriver mDriver;
    //AUTOGEN endpoints
    Endpoint ep_1 = new Endpoint("Embedded", Endpoint.TYPE.SOCKET);
    Endpoint ep_2 = new Endpoint("Gateway", Endpoint.TYPE.SOCKET);
    //USED for test
    String mName ;
    public AppDispatcher(String name){
        this.mName = name;
        model_id_1 = new Model(this);
        mcntr_id_1 = new ModelController(model_id_1);
        mDriver = new AndroidDriver(this);
        switch (mName){
            case "EMD":
                mcntr_id_1.setName("EMD");
                ep_1 = new Endpoint("Gateway", Endpoint.TYPE.SOCKET, "127.0.0.1", 4444);
                mDriver = new AndroidDriver(this);
                mDriver.register_endpoint(ep_1);
                model_id_1.setEndpoint(ep_1);
                break;
            case "GTW":
                mcntr_id_1.setName("GTW");
                ep_1 = new Endpoint("Embedded", Endpoint.TYPE.SOCKET);
                mDriver.register_endpoint(ep_1);
                ep_2 = new Endpoint("Cloud", Endpoint.TYPE.SOCKET);
                mDriver.register_endpoint(ep_2);
                model_id_1.setEndpoint(ep_1);
                model_id_1.setEndpoint(ep_2);
                break;
            case "CLD":
                mcntr_id_1.setName("CLD");
                ep_1 =new Endpoint("Gateway", Endpoint.TYPE.SOCKET , "127.0.0.1", 4444);
                mDriver.register_endpoint(ep_1);
                model_id_1.setEndpoint(ep_1);
                break;
            default:
                System.out.println("OPS");
                break;
        }
        mDriver.appDispatcherReady();
    }


    public Context send_data(byte[] record, Endpoint endpoint){
        //send data to the driver
        new Model.Record()
        Context ctx = mDriver.send_data(record.getData(), endpoint);
        ctx.mRecord = record;
        return ctx;
    }
    @Override
    public void data_received(byte[] data, Endpoint endpoint) {
        //is it an ACK?
        System.out.println("Received data from: " + endpoint.getName());
        //Dispatch to appropriate model
        RavelPacket rp = new RavelPacket(data);
        switch (rp.model_id){
            case 1:
                model_id_1.record_arrived(rp, endpoint);

        }

    }

    @Override
    public void started() {
        //AUTOGEN: controllers that subscribe to the event
        System.out.println("SYS Started: " + mName);
        if(this.mName == "EMD") mcntr_id_1.start = true;
        mcntr_id_1.system_started();
    }

    @Override
    public void stopped() {

    }

    @Override
    public void restarted() {

    }

    @Override
    public void battery(BatteryLevel bl) {

    }

    public String getName() {
        return mName;
    }
}
