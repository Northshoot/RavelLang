package patterns.src.java.rrt;

import patterns.src.java.controller.ModelController;
import patterns.src.java.model.Model;
import patterns.src.java.tiers.Endpoint;


import java.util.Map;

/**
 * Created by lauril on 1/23/17.
 */
public class AppDispatcher implements DispatherAPI{

    Map<String, Model> mModels;
    Map<String, ModelController> mController;
    Map<String, Endpoint> mEndpoints;

    Driver mDriver;

    public AppDispatcher(){

    }

    public void setDriver(Driver d){ this.mDriver = d;}
    public Context send_data(Model.Record record, Endpoint endpoint){

        //send data to the driver
        return null;
    }
    @Override
    public void data_received(byte[] data, Endpoint endpoint) {
        //is it an ACK?
        System.out.println("Received data from: " + endpoint.getName());
        //Dispatch to appropriate model
    }
}
