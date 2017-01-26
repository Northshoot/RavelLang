package patterns.src.java.model;

import patterns.src.java.tiers.Endpoint;

/**
 * Created by lauril on 1/23/17.
 */
public interface ModelBottomAPI {

    void record_arrived(Model.Record record, Endpoint endpoint);

    void record_departed(Model.Record record, Endpoint endpoint);

    void record_saved_durably(Model.Record record);

    void record_saved_endpoint(Model.Record record, Endpoint endpoint);

    void endpoint_connected(Endpoint endpoint);

}
