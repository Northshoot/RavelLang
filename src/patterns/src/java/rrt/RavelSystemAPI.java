package patterns.src.java.rrt;

import patterns.src.java.tiers.Endpoint;

/**
 * Created by lauril on 1/23/17.
 */
public interface RavelSystemAPI {

    Context send_data(byte[] data, Endpoint endpoint);

    void data_received(byte[] data, Endpoint endpoint);
}
