package patterns.src.java.rrt;

import patterns.src.java.model.Record;
import patterns.src.java.tiers.Endpoint;

/**
 * Created by lauril on 1/23/17.
 */
public interface DispatherAPI {

    void data_received(byte[] data, Endpoint endpoint);



}
