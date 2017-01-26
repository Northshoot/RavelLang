package patterns.src.java.rrt;

import patterns.src.java.model.Model;
import patterns.src.java.tiers.Endpoint;

import java.util.Timer;

/**
 * Created by lauril on 1/23/17.
 */
public interface DriverAPI {

    /**
     * Send data to the particular endpoint
     * @param record
     * @param endpoint
     * @return
     */
    Context send_data(Model.Record record, Endpoint endpoint);

    /**
     * Get system timer
     */
    Timer get_timer();

    /**
     * Register endpoint
     */

    void register_endpoint(Endpoint endpoint);
}
