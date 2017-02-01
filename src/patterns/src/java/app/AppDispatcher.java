package patterns.src.java.app;

import org.stanford.ravel.rrt.*;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.HttpEndpoint;
import patterns.src.java.controller.ModelController;
import patterns.src.java.model.Model;
import patterns.src.java.sources.TimerSource1;
import org.stanford.ravel.rrt.tiers.AndroidDriver;
import org.stanford.ravel.rrt.tiers.Error;


import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * Created by lauril on 1/23/17.
 */
public class AppDispatcher  extends AbstractDispatcher {

    //AUTOGEN
    Model model_id_1 ;
    ModelController mcntr_id_1 ;

    Map<String, Endpoint> mEndpoints;

    AndroidDriver mDriver;
    //AUTOGEN endpoints
    Endpoint ep_1 ;
    Endpoint ep_2;
    //USED for test

    //AUTOGEN: system components
    TimerSource1 timerSource;

    String mName ;

    public String getAppName(){ return this.mName ;}


    public AppDispatcher(String name) {

        this.mName = name;
        //AUTOGEN: create models
        model_id_1 = new Model(this);
        //AUTOGEN: interfaces
        timerSource = new TimerSource1();
        //AUTOGEN: create controllers
        mcntr_id_1 = new ModelController(model_id_1, timerSource);
        //AUTOGEN: push controller to all the interfaces and models
        model_id_1.setModelController(mcntr_id_1);
        timerSource.setModelController(mcntr_id_1);

        //AUTOGEN: create system driver
        mDriver = new AndroidDriver(this);
        mcntr_id_1.setName(mName);
        switch (mName){
            case "EMD":
                ep_1 = new Endpoint("Gateway", Endpoint.TYPE.SOCKET, "127.0.0.1", 4444);
                mDriver.register_endpoint(ep_1);
                model_id_1.setEndpointUpp(ep_1);
                break;
            case "GTW":
                ep_1 = new Endpoint("Embedded", Endpoint.TYPE.SOCKET);
                mDriver.register_endpoint(ep_1);
                ep_2 = new HttpEndpoint("Cloud", "http://127.0.0.1:8000/");
                mDriver.register_endpoint(ep_2);
                model_id_1.setEndpointDown(ep_1);
                model_id_1.setEndpointUpp(ep_2);
                break;
            case "CLD":
                ep_1 =new Endpoint("Gateway", Endpoint.TYPE.SOCKET , "127.0.0.1", 4444);
                mDriver.register_endpoint(ep_1);
                model_id_1.setEndpointDown(ep_1);
                break;
            default:
                System.out.println("OPS");
                break;
        }
        mDriver.appDispatcherReady();
    }

    @Override
    public Endpoint getEndpointByName(String name) {
        // FIXME should not create new objects
        switch (name) {
            case "Gateway":
                return new Endpoint("Gateway", Endpoint.TYPE.SOCKET, "127.0.0.1", 4444);
            case "Cloud":
                return new HttpEndpoint("Cloud", "http://127.0.0.1:8000/");
            case "Embedded":
                return new Endpoint("Embedded", Endpoint.TYPE.SOCKET);
            default:
                throw new IllegalArgumentException("Invalid endpoint name " + name);
        }
    }

    private void pprint(String s){
        System.out.println("[" + this.mName +"::AppDispatcher]>" + s);
    }

    /***********************************************************************/
    /*************** AD Commands from model to AD **************************/
    /***********************************************************************/

    @Override
    public Error model__sendData(RavelPacket pkt, Endpoint endpoint){
        //send data to the driver
        //TODO: fix form sim
        int src =0;
        int dst = 0;
        switch (mName){
            case "EMD":
                src = 11111111;
                dst = 22222222;
                break;
            case "GTW":
                src = 22222222;
                dst = 11111111;
                break;
        }
        pkt.src = src;
        pkt.dst = dst;
        pprint("pkt to send: " + pkt);

        return super.model__sendData(pkt, endpoint);
    }

    /***********************************************************************/
    /************** AD callbacks to the models ****************************/
    /***********************************************************************/

    protected void models__notifyDeparted(Event event){
        byte[] data = ((NetworkEvent) event).data;
        Endpoint endpoint = ((NetworkEvent) event).endpoint;
        RavelPacket rp = RavelPacket.fromNetwork(data);
        switch (rp.model_id){
            case 1:
                model_id_1.record_departed(rp, endpoint);

        }
    }

    protected void models__notifyArrived(Event event){
        byte[] data = ((NetworkEvent) event).data;
        Endpoint endpoint = ((NetworkEvent) event).endpoint;
        RavelPacket rp = RavelPacket.fromNetwork(data);
        pprint("Received data from: " + endpoint.getName() + " pkt:" + rp);
        switch (rp.model_id){
            case Model.MODEL_ID:
                model_id_1.record_arrived(rp, endpoint);

        }
    }

    /***********************************************************************/
    /************** Network callbacks from AD to Driver ********************/
    /***********************************************************************/
    public void driver__sendData(Event event) {
        mDriver.sendData(((NetworkEvent) event).data,
                        ((NetworkEvent) event).endpoint);
    }


    /***********************************************************************/
    /************** Network callbacks from Driver to AD ********************/
    /***********************************************************************/
    @Override
    public void driver__sendDone(Error networkError, byte[] data, Endpoint endpoint) {
        pprint("driver_send_done, ERROR: " + networkError);
        super.driver__sendDone(networkError, data, endpoint);
    }


    /***********************************************************************/
    /************** System callbacks from Driver to AD *********************/
    /***********************************************************************/
    /**
     * System has started
     */
    @Override
    public void started() {
        //AUTOGEN: controllers that subscribe to the event
        pprint("SYS Started: " + mName);
        if(this.mName == "EMD") mcntr_id_1.start = true;
        mcntr_id_1.system_started();
    }
    /**
     * System has stopped
     */
    @Override
    public void stopped() {

    }
    /**
     * System has restarted
     */
    @Override
    public void restarted() {

    }
    /**
     * Low battery indicator
     */
    @Override
    public void battery(BatteryLevel bl) {

    }


}
