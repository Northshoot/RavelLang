package patterns.src.java.rrt;

import patterns.src.java.controller.ModelController;
import patterns.src.java.model.Model;
import patterns.src.java.tiers.Endpoint;


import java.util.Map;

/**
 * Created by lauril on 1/23/17.
 */
public class AppDispatcher implements DispatherAPI, SystemEventAPI{


    //AUTOGEN
    Model model_id_1 = new Model(this);
    ModelController mcntr_id_1 = new ModelController(model_id_1);

    Map<String, Endpoint> mEndpoints;

    Driver mDriver;

    public AppDispatcher(){

    }

    public void setDriver(Driver d){ this.mDriver = d;}
    public Context send_data(byte[] record, Endpoint endpoint){
        //send data to the driver
        return null;
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
}
