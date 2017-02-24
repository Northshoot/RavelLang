package edu.stanford.ledcontrol.model;

import java.text.DateFormat;
import java.util.Date;

import edu.stanford.ravel.controller.LedStatusController;
import edu.stanford.ravel.defines.RavelDefines;
import edu.stanford.ravel.model.RavelAbstractModel;


/**
 * Created by lauril on 2/10/16.
 */
public class LedStatusRepresentation extends RavelAbstractModel{

        public final String model_id;
        public final int led_status;
        public final String gateway_id;
        public final String origin;
        public final String time;

    @Override
    public String toString() {
        return "LedStatusRepresentation{" +
//                "model_id='" + model_id + '\'' +
                ", led_status=" + led_status +
                ", iot_device='" + gateway_id + '\'' +
                ", origin='" + origin + '\'' +
                ", time='" + time + '\'' +
                '}';
    }




        public LedStatusRepresentation(int led_status, String iot_device, String origin) {
            this(LedStatusController.model_id, led_status, iot_device,  origin);

        }

        public LedStatusRepresentation(String model_id, int led_status, String gateway_id, String origin) {
            this(model_id, led_status, gateway_id,  origin,
                    DateFormat.getTimeInstance().format(new Date()));
        }
        public LedStatusRepresentation(String model_id, int led_status, String gateway_id, String origin, String time){
            super();
            this.model_id = model_id;
            this.led_status = led_status;
            this.gateway_id = RavelDefines.PHONE_ID;
            this.origin = origin;
            this.time = time;

        }



}
