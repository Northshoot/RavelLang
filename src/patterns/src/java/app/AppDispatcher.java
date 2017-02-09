package patterns.src.java.app;

import org.stanford.ravel.rrt.AbstractDispatcher;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.events.NetworkEvent;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;
import org.stanford.ravel.rrt.tiers.JavaDriver;
import patterns.src.java.controller.ModelController;
import patterns.src.java.model.Model;
import patterns.src.java.sources.TimerSource1;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by lauril on 1/23/17.
 */
public class AppDispatcher  extends AbstractDispatcher {

    //AUTOGEN
    Model model_id_1 ;
    ModelController mcntr_id_1 ;

    Map<String, Endpoint> mEndpoints;

    JavaDriver mDriver;
    //AUTOGEN: endpoints
    Endpoint embeddedEndpoint ;
    Endpoint gatewayEndpoint;
    Endpoint cloudEndpoint;
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
        mDriver = new JavaDriver(this);
        mcntr_id_1.setName(mName);

        try {
            cloudEndpoint = Endpoint.fromString("Cloud", new URI("http://127.0.0.1:8000/api/push"), null);
            gatewayEndpoint = Endpoint.fromString("Gateway", new URI("tcp://127.0.0.1:4444"), null);
            embeddedEndpoint = Endpoint.fromString("Embedded", new URI("tcp://127.0.0.1:5555"), null);

            Error err = mDriver.registerEndpoint(cloudEndpoint);
            assert err == Error.SUCCESS;
            err = mDriver.registerEndpoint(gatewayEndpoint);
            assert err == Error.SUCCESS;
            err = mDriver.registerEndpoint(embeddedEndpoint);
            assert err == Error.SUCCESS;
        } catch(URISyntaxException|MalformedURLException e) {
            throw new AssertionError(e);
        }

        switch (mName){
            case "Embedded":
                // embedded sends to gateway
                model_id_1.addEndpoints(Collections.singletonList("Gateway"));
                break;
            case "Gateway":
                // gateway sends to cloud
                model_id_1.addEndpoints(Collections.singletonList("Cloud"));
                break;
            case "Cloud":
                // cloud does not send anywhere
                break;
            default:
                throw new AssertionError();
        }

        mDriver.appDispatcherReady();
    }

    private void pprint(String s) {
        System.out.println("[" + this.mName +"::AppDispatcher]>" + s);
    }

    /***********************************************************************/
    /*************** AD Commands from model to AD **************************/
    /***********************************************************************/

    @Override
    public Error model__sendData(RavelPacket pkt, Endpoint endpoint) {
        //send data to the driver
        //TODO: fix form sim
        int src =0;
        int dst = 0;
        switch (mName) {
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

        return mDriver.sendData(pkt, endpoint);
    }

    /***********************************************************************/
    /************** AD callbacks to the models ****************************/
    /***********************************************************************/

    protected void models__notifyDeparted(NetworkEvent event) {
        RavelPacket rp = event.data;
        Endpoint endpoint = event.endpoint;
        switch (rp.model_id){
            case 1:
                model_id_1.recordDeparted(rp, endpoint);
        }
    }

    protected void models__notifyArrived(NetworkEvent event) {
        RavelPacket rp = event.data;
        Endpoint endpoint = event.endpoint;
        pprint("Received data from: " + endpoint.getName() + " pkt:" + rp);
        switch (rp.model_id){
            case Model.MODEL_ID:
                model_id_1.recordArrived(rp, endpoint);
        }
    }

    protected void models__notifyFailedToSend(NetworkEvent event) {
        RavelPacket rp = event.data;
        Endpoint endpoint = event.endpoint;
        Error error = event.error;
        switch (rp.model_id){
            case Model.MODEL_ID:
                model_id_1.recordFailedToSend(rp, endpoint, error);
        }
    }

    @Override
    public Collection<Endpoint> getEndpointsByName(String name) {
        return mDriver.getEndpointsByName(name);
    }


    /***********************************************************************/
    /************** Network callbacks from Driver to AD ********************/
    /***********************************************************************/
    @Override
    public void driver__sendDone(Error networkError, RavelPacket data, Endpoint endpoint) {
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
        if(this.mName.equals("Embedded")) mcntr_id_1.start = true;
        mcntr_id_1.system_started();
    }
    /**
     * System has stopped
     */
    @Override
    public void stopped() {
        //AUTOGEN: list of controllers subscribing to the event
    }
    /**
     * System has restarted
     */
    @Override
    public void restarted() {
        //AUTOGEN: list of controllers subscribing to the event
    }
    /**
     * Low battery indicator
     */
    @Override
    public void battery(BatteryLevel bl) {

    }


}
