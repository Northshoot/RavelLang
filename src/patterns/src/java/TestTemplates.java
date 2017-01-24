package patterns.src.java;

import patterns.src.java.controller.ModelController;
import patterns.src.java.model.Model;
import patterns.src.java.rrt.AppDispatcher;
import patterns.src.java.tiers.AndroidDriver;
import patterns.src.java.tiers.Endpoint;

/**
 * Created by lauril on 1/23/17.
 */
public class TestTemplates {



    public static void main(String [] args)
    {
        //AppDispather
        AppDispatcher appD = new AppDispatcher();
        //create model
        Model m = new Model(appD);
        //create controller
        ModelController mcntr = new ModelController(m);
        //create endpoint
        Endpoint ep0 = new Endpoint("Embedded", Endpoint.TYPE.SOCKET);
        Endpoint ep1 = new Endpoint("Cloud", Endpoint.TYPE.SOCKET);
        //create driver
        AndroidDriver androidDriver = new AndroidDriver(appD);

        androidDriver.register_endpoint(ep0);
        androidDriver.register_endpoint(ep1);
        //test diver
        androidDriver.rx_data_from_socket(new byte[]{1,1,1,1,1,1});
        //test model API

        //test controller API
    }
}
