package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.tiers.Endpoint;
/**
 * The API exposed by the "system" object in the controllers.
 *
 * Created by lauril on 1/26/17.
 */
public interface SystemEventAPI {
    /**
     * Print a message to standard error, or wherever the most appropriate
     * for the platform
     *
     * @param msg the message to print
     */
    void print(String msg);

    /**
     * Event that are called by system driver, has to be implemented in
     * Event consumer
     */
    void started();
    void stopped();
    void restarted();
    void connected(Endpoint endpoint);
    void disconected(Endpoint endpoint);

    enum BatteryLevel {HIGH, HIGH_MID, MID, MID_LOW, LOW, CRITICAL }
    void battery(BatteryLevel bl);





}
